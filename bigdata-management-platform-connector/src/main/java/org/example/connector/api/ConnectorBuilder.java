package org.example.connector.api;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ConnectorBuilder {
    DatasourceType supportType();

    Connector buildConnector(String paramStr) throws JsonProcessingException;
}
