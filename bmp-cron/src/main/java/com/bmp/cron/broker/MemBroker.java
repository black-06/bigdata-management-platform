package com.bmp.cron.broker;

import com.bmp.cron.Task;

import java.util.concurrent.LinkedBlockingQueue;

public class MemBroker implements Broker {
    private final LinkedBlockingQueue<Task> queue;
    private final int consumerSize;

    public MemBroker(int consumerSize) {
        this.queue = new LinkedBlockingQueue<>();
        this.consumerSize = consumerSize;
    }

    @Override
    public void send(Task task) {
        queue.add(task);
    }

    @Override
    public void startConsume() {
        for (int i = 0; i < consumerSize; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        Task.execute(queue.take());
                    } catch (InterruptedException ignored) {
                    }
                }
            });
            thread.setName("consumer_" + i);
            thread.start();
        }
    }
}
