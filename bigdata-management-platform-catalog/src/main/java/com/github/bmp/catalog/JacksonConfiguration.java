package com.github.bmp.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bmp.connector.api.ConnectorManager;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public JacksonConfiguration(ObjectMapper objectMapper) {
        ConnectorManager.registerSubtypes(objectMapper);
    }
}
