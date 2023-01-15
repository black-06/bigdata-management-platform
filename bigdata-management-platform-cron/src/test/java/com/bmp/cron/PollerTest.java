package com.bmp.cron;

import com.bmp.cron.broker.Broker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PollerTest {

    @Test
    public void startSend() {
        int size = 15;
        CountDownLatch latch = new CountDownLatch(size);
        List<Task> tasks = new ArrayList<>();

        Timer timer = Poller.start(
                new Broker() {
                    @Override
                    public void send(Task task) {
                        tasks.add(task);
                        latch.countDown();
                    }

                    @Override
                    public void startConsume() {

                    }
                },
                // retriever
                () -> Stream.iterate(0, i -> i + 1).limit(size).collect(Collectors.toList()),
                // builder
                integer -> mockTask(),
                // post add
                task -> {
                }
        );

        // wait group
        assertDoesNotThrow(() -> {
            latch.await();
            timer.cancel();
        });

        assertEquals(size, tasks.size());
    }


    public Task mockTask() {
        Task mock = Mockito.mock(Task.class);
        Mockito.when(mock.getId()).thenReturn("mock_id");
        Mockito.when(mock.type()).thenReturn("mock");
        Mockito.when(mock.getExecuteDelay()).thenReturn(Duration.ZERO);
        Mockito.when(mock.getRecurringInterval()).thenReturn(Duration.ofHours(1));
        return mock;
    }

}