package com.bmp.connector.fake;

import com.bmp.commons.enums.DatasourceType;
import com.bmp.connector.api.Connector;
import com.bmp.connector.api.ConnectorInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.service.AutoService;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AutoService(ConnectorInfo.class)
public class FakeConnectorInfo implements ConnectorInfo {
    private String foo;

    @JsonCreator
    public FakeConnectorInfo(@JsonProperty("foo") String foo) {
        this.foo = foo;
    }

    @Override
    public DatasourceType supportType() {
        return DatasourceType.FAKE;
    }

    @Override
    public Connector buildConnector() {
        return new FakeConnector();
    }
}
