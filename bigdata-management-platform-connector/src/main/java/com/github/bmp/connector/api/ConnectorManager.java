package com.github.bmp.connector.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.ServiceLoader;

/**
 * It manages {@link ConnectorInfo} by SPI.
 */
public class ConnectorManager {

    public static final ObjectMapper mapper = new ObjectMapper();

    static {
        registerSubtypes(mapper);
    }

    public static void registerSubtypes(ObjectMapper mapper) {
        mapper.registerModule(new JavaTimeModule());
        for (ConnectorInfo builder : ServiceLoader.load(ConnectorInfo.class)) {
            mapper.registerSubtypes(new NamedType(builder.getClass(), builder.supportType().name()));
        }
    }

    /**
     * unmarshal json sting to connector info.
     *
     * @param json connector info json string
     * @return info object
     * @throws JsonProcessingException if jackson fails.
     */
    public static ConnectorInfo unmarshalConnectorInfo(String json) throws JsonProcessingException {
        return mapper.readValue(json, ConnectorInfo.class);
    }

    /**
     * marshal connector info to json string.
     *
     * @param info connector info object
     * @return info json
     * @throws JsonProcessingException if jackson fails.
     */
    public static String marshalConnectorInfo(ConnectorInfo info) throws JsonProcessingException {
        return mapper.writeValueAsString(info);
    }

    public static <T> T unmarshal(String json, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(json, clazz);
    }
}
