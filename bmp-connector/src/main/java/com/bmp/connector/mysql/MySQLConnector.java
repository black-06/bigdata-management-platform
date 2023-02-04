package com.bmp.connector.mysql;

import com.bmp.connector.api.Connector;
import com.bmp.connector.api.RowIterator;
import com.bmp.connector.api.alignment.Alignment;
import com.bmp.connector.api.alignment.AlignmentByName;
import com.bmp.connector.api.alignment.IColumn;
import com.bmp.connector.api.list.Lister;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MySQLConnector implements Connector {
    private final MySQLConnectorInfo info;

    @Override
    public void ping() {
        try {
            MySQLUtils.open(info).close();
        } catch (SQLException e) {
            throw new IllegalArgumentException("test connection failed");
        }
    }

    @Override
    public Lister getLister() {
        return new MySQLLister(info);
    }

    @Override
    public <A extends IColumn, B extends IColumn> Alignment<A, B> getAlignment() {
        return new AlignmentByName<>();
    }

    @Override
    public RowIterator getRowIterator() {
        return new MySQLRowIterator(info);
    }
}
