package com.github.bmp.cron;

import com.google.auto.service.AutoService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    private static final Logger logger = LoggerFactory.getLogger(TaskTest.class);

    @Test
    public void serializeTask() {
        MockTask mockTask = new MockTask();
        mockTask.setOther(15);
        mockTask.setMaxInterval(30_000);

        byte[] json = Assertions.assertDoesNotThrow(mockTask::serialize);
        logger.info("json is {}", new String(json));

        Task task = assertDoesNotThrow(() -> Task.deserialize(json));
        MockTask actual = assertInstanceOf(MockTask.class, task);
        assertEquals(mockTask, actual);
    }

    @AllArgsConstructor
    private static class ValidateTest {
        private Task task;
        private String exception;
    }

    public static Stream<ValidateTest> factory() {
        List<ValidateTest> tests = new ArrayList<>();
        tests.add(new ValidateTest(null, "task is null"));
        tests.add(new ValidateTest(new MockTask().setRecurring(true), "task recurring interval is null"));
        tests.add(new ValidateTest(
                new MockTask().setRecurring(true).setPeriod(Duration.ofMillis(1)),
                String.format("task recurring interval is wrong, it should be greater than %s", Task.minimalInterval)
        ));
        tests.add(new ValidateTest(new MockTask().setRecurring(false).setPeriod(Duration.ZERO), null));
        tests.add(new ValidateTest(new MockTask().setRecurring(true).setPeriod(Duration.ofHours(1)), null));
        return tests.stream();
    }

    @ParameterizedTest
    @MethodSource("factory")
    public void validateTask(ValidateTest test) {
        logger.info(test.exception);
        if (test.exception == null) {
            assertDoesNotThrow(() -> Task.validate(test.task));
        } else {
            IllegalArgumentException actual = assertThrows(IllegalArgumentException.class, () -> Task.validate(test.task));
            assertEquals(test.exception, actual.getMessage());
        }
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @AutoService(Task.class)
    public static class MockTask extends Task {
        private int other;

        @Override
        public String type() {
            return "mock";
        }

        @Override
        public void run() {
        }
    }
}