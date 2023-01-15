package com.github.bmp.connector.api;

import com.github.bmp.connector.api.alignment.Alignment;
import com.github.bmp.connector.api.alignment.IColumn;
import com.github.bmp.connector.api.list.Lister;

import java.sql.SQLException;

public interface Connector {
    void ping() throws SQLException;

    Lister getLister();

    <A extends IColumn, B extends IColumn> Alignment<A, B> getAlignment();
}


