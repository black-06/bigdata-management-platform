package com.bmp.connector.hive;

import com.bmp.connector.api.RowIterator;

public class HiveRowIterator implements RowIterator {
    private final HiveConnectorInfo info;

    public HiveRowIterator(HiveConnectorInfo info) {
        this.info = info;
    }
}
