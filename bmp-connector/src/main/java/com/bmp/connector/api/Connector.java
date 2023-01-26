package com.bmp.connector.api;

import com.bmp.connector.api.alignment.Alignment;
import com.bmp.connector.api.alignment.IColumn;
import com.bmp.connector.api.list.Lister;

import java.sql.SQLException;

/**
 * Connector interface for any 2d datasource.
 */
public interface Connector {
    void ping() throws SQLException;

    Lister getLister();

    <A extends IColumn, B extends IColumn> Alignment<A, B> getAlignment();
}


