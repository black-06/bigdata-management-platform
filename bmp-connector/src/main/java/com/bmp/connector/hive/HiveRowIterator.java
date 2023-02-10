package com.bmp.connector.hive;

import com.bmp.connector.api.LimitPushdown;
import com.bmp.connector.api.RowData;
import com.bmp.connector.api.RowIterator;
import com.bmp.connector.api.alignment.IColumn;
import com.bmp.connector.api.list.AssetPath;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class HiveRowIterator<C extends IColumn> extends LimitPushdown implements RowIterator {
    private final List<C> columns;
    private final Connection connection;
    private final ResultSet results;

    public HiveRowIterator(HiveConnectorInfo info, AssetPath path, List<C> columns) {
        this.columns = columns;
        this.connection = HiveUtils.open(info);
        try {
            Statement statement = this.connection.createStatement();
            this.results = statement.executeQuery(HiveUtils.buildQuery(path, columns, super.limit));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean next() throws Exception {
        return this.results.next();
    }

    @Override
    public RowData getRow() throws Exception {
        int arity = this.columns.size();
        RowData row = new RowData(arity);
        for (int i = 0; i < arity; i++) {
            row.setValue(i, this.results.getObject(i));
        }
        return row;
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }
}
