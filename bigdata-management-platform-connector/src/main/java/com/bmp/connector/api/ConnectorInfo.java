package com.bmp.connector.api;

import com.bmp.commons.enums.DatasourceType;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * It contains connector information to build a connector.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface ConnectorInfo {
    /**
     * support connector type, it's stored in json "type" field
     * that used for jackson serialize and deserialize.
     */
    DatasourceType supportType();

    Connector buildConnector();
}
