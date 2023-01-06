package org.example.connector.api;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum DatasourceType {
    MySQL,
    ;

    public static DatasourceType ofName(String name) {
        return Arrays.stream(DatasourceType.values())
                .filter(type -> StringUtils.equalsIgnoreCase(type.name(), name))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("no such datasource type"));
    }
}
