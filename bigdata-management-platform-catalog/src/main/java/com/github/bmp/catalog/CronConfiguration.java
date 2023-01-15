package com.github.bmp.catalog;

import com.github.bmp.cron.broker.Broker;
import com.github.bmp.cron.broker.MemBroker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CronConfiguration {
    @Bean
    public Broker broker(@Value("${platform.mem.consumer-size}") int consumerSize) {
        MemBroker broker = new MemBroker(consumerSize);
        broker.startConsume();
        return broker;
    }
}
