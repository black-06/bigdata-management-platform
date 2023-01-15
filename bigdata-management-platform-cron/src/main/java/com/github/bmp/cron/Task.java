package com.github.bmp.cron;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.bmp.commons.Validate;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.support.RetryTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.util.ServiceLoader;
import java.util.UUID;

@Data
@Accessors(chain = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class Task implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Task.class);

    private String id = UUID.randomUUID().toString();
    private Duration period;
    private boolean recurring;

    /* retry config */

    /**
     * It is the amount of time to backoff after the first failure in milliseconds.
     * Default is 1_000 millisecond.
     */
    private long initialInterval = 1_000;
    /**
     * Multiplier is the factor with which to multiply backoffs after a
     * failed retry. Should ideally be greater than 1.
     */
    private double multiplier = 1.6;
    /**
     * The maximum value of the backoff period in milliseconds.
     * Default value is 30_000 milliseconds.
     */
    private long maxInterval = 30_000;

    /**
     * Timeout is the upper bound of task execution in milliseconds.
     * Default value is 5 minutes.
     */
    private long timeout = 5 * 60_000;

    /**
     * it's stored in json "type" field
     * that used for jackson serialize and deserialize.
     */
    public abstract String type();

    /**
     * Run the task in a separate thread.
     */
    public abstract void run() throws Exception;

    /**
     * set minimal recurring duration to avoid too frequently.
     */
    public static final Duration minimalInterval = Duration.ofSeconds(1);

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        for (Task task : ServiceLoader.load(Task.class)) {
            mapper.registerSubtypes(new NamedType(task.getClass(), task.type()));
        }
    }

    public byte[] serialize() throws IOException {
        return mapper.writeValueAsBytes(this);
    }

    public static Task deserialize(byte[] json) throws IOException {
        return mapper.readValue(json, Task.class);
    }

    public static void validate(Task task) {
        Validate.notNull(task, "task is null");
        Validate.notNull(task.getId(), "task id is null");
        Validate.notNull(task.type(), "task type is null");
        Duration interval = Validate.notNull(task.getPeriod(), "task recurring interval is null");
        if (task.isRecurring() && interval.compareTo(minimalInterval) < 0) {
            throw new IllegalArgumentException(String.format("task recurring interval is wrong, it should be greater than %s", minimalInterval));
        }
    }


    public static void execute(Task task) {
        try {
            validate(task);
            RetryTemplate.builder()
                    .withinMillis(task.getTimeout())
                    .exponentialBackoff(task.getInitialInterval(), task.getMultiplier(), task.getMaxInterval(), true)
                    .build()
                    .execute(context -> {
                        task.run();
                        return null;
                    });
        } catch (Throwable e) {
            logger.error("run task failed", e);
        }
    }
}

