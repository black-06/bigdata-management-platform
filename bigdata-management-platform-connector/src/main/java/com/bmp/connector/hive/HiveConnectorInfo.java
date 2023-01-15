package com.bmp.connector.hive;

import com.bmp.commons.enums.DatasourceType;
import com.bmp.connector.api.Connector;
import com.bmp.connector.api.ConnectorInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.service.AutoService;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AutoService(ConnectorInfo.class)
public class HiveConnectorInfo implements ConnectorInfo {
    private String host;
    private String port;
    private String username;
    private String password;
    @JsonIgnore
    private String jdbcUrl;

    @JsonCreator
    public HiveConnectorInfo(
            @JsonProperty("host") String host,
            @JsonProperty("port") String port,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password
    ) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.jdbcUrl = String.format("jdbc:hive2://%s:%s", host, port);
    }

    @Override
    public DatasourceType supportType() {
        return DatasourceType.HIVE;
    }

    @Override
    public Connector buildConnector() {
        return new HiveConnector(this);
    }
}
