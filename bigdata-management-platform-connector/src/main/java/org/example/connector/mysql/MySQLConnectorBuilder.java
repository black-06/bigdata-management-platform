package org.example.connector.mysql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auto.service.AutoService;
import org.example.connector.api.Connector;
import org.example.connector.api.ConnectorBuilder;
import org.example.connector.api.DatasourceType;

@AutoService(ConnectorBuilder.class)
public class MySQLConnectorBuilder implements ConnectorBuilder {
    @Override
    public DatasourceType supportType() {
        return DatasourceType.MySQL;
    }

    @Override
    public Connector buildConnector(String paramStr) throws JsonProcessingException {
        MySQLParam param = new ObjectMapper().readValue(paramStr, MySQLParam.class);
        return new MySQLConnector(param);
    }
}
