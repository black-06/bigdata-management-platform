package com.github.bmp.connector.hive;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bmp.connector.api.ConnectorInfo;
import com.github.bmp.connector.api.ConnectorManager;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class HiveConnectorInfoTest {
    private static final Logger logger = LoggerFactory.getLogger(HiveConnectorInfoTest.class);

    @Test
    public void testJsonCreator() {
        ObjectMapper mapper = new ObjectMapper();
        HiveConnectorInfo info = new HiveConnectorInfo("127.0.0.1", "10000", "root", "root");
        String value = assertDoesNotThrow(() -> mapper.writeValueAsString(info));

        String jdbcUrl = assertDoesNotThrow(() -> mapper.readTree(value).path("jdbcUrl").asText());
        assertEquals("", jdbcUrl);

        HiveConnectorInfo actual = assertDoesNotThrow(() -> mapper.readValue(value, HiveConnectorInfo.class));
        assertEquals("jdbc:hive2://127.0.0.1:10000", actual.getJdbcUrl());
    }

    @Test
    void testMarshalConnectorInfo() {
        HiveConnectorInfo info = new HiveConnectorInfo("127.0.0.1", "3306", "root", "123");
        String json = assertDoesNotThrow(() -> ConnectorManager.marshalConnectorInfo(info));

        logger.info("json is {}", json);

        ConnectorInfo actual = assertDoesNotThrow(() -> ConnectorManager.unmarshalConnectorInfo(json));
        assertInstanceOf(HiveConnectorInfo.class, actual);
    }
}