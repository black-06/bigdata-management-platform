package com.bmp.cron.broker;

import com.bmp.cron.Task;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class MemBrokerTest {

    private static final Logger logger = LoggerFactory.getLogger(MemBrokerTest.class);

    @Test
    void send() {
        int size = 10;
        CountDownLatch latch = new CountDownLatch(size);

        Broker broker = new MemBroker(3);
        broker.startConsume();
        for (int i = 0; i < size; i++) {
            Task task = mockTask();

            int finalI = i;
            assertDoesNotThrow(() -> Mockito.doAnswer(invocation -> {
                Thread.sleep(finalI * 100);
                latch.countDown();
                logger.info("count down: {}", finalI);
                return null;
            }).when(task).run());

            assertDoesNotThrow(() -> broker.send(task));
        }

        // wait group
        assertDoesNotThrow(() -> latch.await(1, TimeUnit.MINUTES));
    }

    public Task mockTask() {
        Task mock = Mockito.mock(Task.class);
        Mockito.when(mock.getId()).thenReturn("mock_id");
        Mockito.when(mock.type()).thenReturn("mock");
        Mockito.when(mock.getExecuteDelay()).thenReturn(Duration.ZERO);
        Mockito.when(mock.getRecurringInterval()).thenReturn(Duration.ofHours(1));
        Mockito.when(mock.getInitialInterval()).thenReturn(1_000L);
        Mockito.when(mock.getMultiplier()).thenReturn(1.6);
        Mockito.when(mock.getMaxInterval()).thenReturn(30_000L);
        Mockito.when(mock.getTimeout()).thenReturn(5 * 60_000L);
        return mock;
    }
}