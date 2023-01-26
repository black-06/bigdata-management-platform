package com.bmp.cron;

import com.bmp.commons.Validate;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    private Duration executeDelay;
    private Duration recurringInterval;

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

    /**
     * TODO: validate the task params. see {@link Validate}
     *
     * @param task task
     */
    public static void validate(Task task) {
    }

    /**
     * TODO:
     *  1. validate task
     *  2. run the task and exp retry when error occurs. see {@link RetryTemplate}
     * <p>
     *  this function should handle all errors without throwing up.
     *
     * @param task task.
     */
    public static void execute(Task task) {
    }
}

