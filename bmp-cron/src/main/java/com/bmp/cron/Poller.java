package com.bmp.cron;

import com.bmp.cron.broker.Broker;
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
     * Poller periodically ({@link #INTERVAL}) does a set of subroutines specified by {@code "retriever"},
     * {@code "taskBuilder"} and {@code postTaskAddition} to find tasks and push them into an underlying
     * message queue for execution. The period is specified by queryInterval.
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
            // TODO: get R list from retriever
            // TODO: build task by builder
            // TODO: schedule by task execute delay
            //       a. send task to broker
            //       b. call PostTaskAddition function
        }, INTERVAL.toMillis(), INTERVAL.toMillis(), TimeUnit.MILLISECONDS);
    }
}
