package com.bmp.cron.broker;

import com.bmp.cron.Task;

import java.util.concurrent.BlockingQueue;

/**
 * broker in memory should have a {@link BlockingQueue} to store tasks.
 * It should have some threads to consume tasks.
 */
public class MemBroker implements Broker {
    public MemBroker(int consumerSize) {
    }

    @Override
    public void send(Task task) {
    }

    @Override
    public void startConsume() {
    }
}
