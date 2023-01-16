package com.bmp.cron;

import com.bmp.cron.broker.Broker;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Poller {
    private static final Logger logger = LoggerFactory.getLogger(Poller.class);
    public static final Duration INTERVAL = Duration.ofSeconds(5);
    private static final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    /**
     * Poller periodically does a set of subroutines specified by {@code "retriever"}, {@code "taskBuilder"} and
     * {@code postTaskAddition} to find tasks and push them into an underlying message queue for execution.
     * The period is specified by queryInterval.
     *
     * @param broker           see {@link Broker}
     * @param retriever        is a subroutine to get task models to be executed from the upper level task
     *                         storage such as a MySql table. It returns a list of task models retrieved to
     *                         hand over to {@code "taskBuilder"} to build executable tasks.
     * @param builder          receives a raw task models and outputs a {@link Task}. It should be used in
     *                         conjunction with {@code "retriever"} as it should know how to cast the type
     *                         of the given raw task model and how to build a {@link Task} from the raw task model.
     * @param postTaskAddition does some finalization after a {@link Task} is pushed into an underlying message queue.
     *                         For example, it may update the original MySql table from which the raw task model
     *                         is retrieved.
     * @param <R>              raw task models
     * @param <T>              {@link Task}
     */
    public static <R, T extends Task> void start(
            Broker broker,
            Supplier<List<R>> retriever,
            Function<R, T> builder,
            Consumer<T> postTaskAddition
    ) {
        logger.info("start poller send");
        service.scheduleAtFixedRate(() -> {
            try {
                List<R> raws = retriever.get();
                if (CollectionUtils.isEmpty(raws)) {
                    return;
                }
                for (R raw : raws) {
                    T task = builder.apply(raw);
                    try {
                        Task.validate(task);
                    } catch (IllegalArgumentException e) {
                        logger.error("validate failed, task: {}", task);
                        continue;
                    }
                    service.schedule(() -> {
                        try {
                            broker.send(task);
                            postTaskAddition.accept(task);
                        } catch (Exception e) {
                            logger.error("send task failed", e);
                        }
                    }, task.getExecuteDelay().toNanos(), TimeUnit.NANOSECONDS);
                }
            } catch (Exception e) {
                logger.error("error encountered where sending task", e);
            }
        }, 0, INTERVAL.toMillis(), TimeUnit.MILLISECONDS);
    }
}
