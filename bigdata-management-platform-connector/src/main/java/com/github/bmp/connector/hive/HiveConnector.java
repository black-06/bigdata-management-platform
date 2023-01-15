package com.github.bmp.connector.hive;

import com.github.bmp.connector.api.Connector;
import com.github.bmp.connector.api.alignment.Alignment;
import com.github.bmp.connector.api.alignment.AlignmentByName;
import com.github.bmp.connector.api.alignment.IColumn;
import com.github.bmp.connector.api.list.Lister;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class HiveConnector implements Connector {

    private final HiveConnectorInfo info;

    @Override
    public void ping() {
        try {
            HiveUtils.open(info).close();
        } catch (SQLException e) {
            throw new IllegalArgumentException("test connection failed");
        }
    }

    @Override
    public Lister getLister() {
        return new HiveLister(info);
    }

    @Override
    public <A extends IColumn, B extends IColumn> Alignment<A, B> getAlignment() {
        return new AlignmentByName<>();
    }
}
