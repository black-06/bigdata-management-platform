package org.example.connector.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class ConnectorManager {
    private static final Map<DatasourceType, ConnectorBuilder> connectors;

    static {
        connectors = new HashMap<>();
        for (ConnectorBuilder builder : ServiceLoader.load(ConnectorBuilder.class)) {
            connectors.put(builder.supportType(), builder);
        }
    }

    public Connector getConnector(String param) throws JsonProcessingException {
        DatasourceType type = DatasourceType.ofName(new ObjectMapper().readTree(param).path("type").asText());
        ConnectorBuilder builder = connectors.get(type);
        return builder.buildConnector(param);
    }
}
