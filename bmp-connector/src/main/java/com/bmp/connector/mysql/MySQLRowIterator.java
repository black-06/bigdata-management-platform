package com.bmp.connector.mysql;

import com.bmp.connector.api.RowIterator;

public class MySQLRowIterator implements RowIterator {
    private final MySQLConnectorInfo info;

    public MySQLRowIterator(MySQLConnectorInfo info) {
        this.info = info;
    }
}
