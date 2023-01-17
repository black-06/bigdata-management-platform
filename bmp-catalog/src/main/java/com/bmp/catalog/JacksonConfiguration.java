package com.bmp.catalog;

import com.bmp.connector.api.ConnectorManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public JacksonConfiguration(ObjectMapper objectMapper) {
        ConnectorManager.registerSubtypes(objectMapper);
    }
}
