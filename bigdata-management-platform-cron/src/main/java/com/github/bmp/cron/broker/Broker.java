package com.github.bmp.cron.broker;

import com.github.bmp.cron.Task;

/**
 * Broker represents a message queue through which we pass around the tasks to be run
 */
public interface Broker {
    /**
     * Producer: It adds an {@link Task} to the underlying message queue.
     * The way to add the task to the message queue should be dependent on the type
     * of the underlying message queue of the scheduler.
     *
     * @param task task
     */
    void send(Task task) throws Exception;

    /**
     * Register a callback to execute on task message arrival for consuming.
     */
    void startConsume();
}
