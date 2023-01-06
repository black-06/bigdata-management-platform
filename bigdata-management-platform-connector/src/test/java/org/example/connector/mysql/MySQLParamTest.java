package org.example.connector.mysql;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MySQLParamTest {

    @Test
    public void testJsonCreator() {
        ObjectMapper mapper = new ObjectMapper();
        MySQLParam param = new MySQLParam("127.0.0.1", "3306", "root", "123");
        String value = assertDoesNotThrow(() -> mapper.writeValueAsString(param));

        String jdbcUrl = assertDoesNotThrow(() -> mapper.readTree(value).path("jdbcUrl").asText());
        assertEquals("", jdbcUrl);

        MySQLParam actual = assertDoesNotThrow(() -> mapper.readValue(value, MySQLParam.class));
        assertEquals("jdbc:mysql://127.0.0.1:3306", actual.getJdbcUrl());
    }
}