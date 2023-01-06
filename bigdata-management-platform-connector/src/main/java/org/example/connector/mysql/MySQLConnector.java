package org.example.connector.mysql;

import lombok.RequiredArgsConstructor;
import org.example.connector.api.Asset;
import org.example.connector.api.AssetPath;
import org.example.connector.api.Column;
import org.example.connector.api.Connector;
import org.example.connector.api.alignment.Alignment;
import org.example.connector.api.alignment.AlignmentByName;
import org.example.connector.api.alignment.IColumn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MySQLConnector implements Connector {
    private final MySQLParam param;

    @Override
    public void ping() throws SQLException {
        try (Connection con = this.getConnection()) {
            if (con == null) {
                throw new SQLException("test connection failed");
            }
        }
    }

    @Override
    public List<Asset> listAssets(AssetPath path) throws SQLException {
        List<Asset> assets = new ArrayList<>();
        try (Connection con = this.getConnection()) {
            // TODO: list assets
        }
        return assets;
    }


    public List<Column> listColumns(AssetPath path) {
        // TODO: list columns
        return null;
    }

    @Override
    public <A extends IColumn, B extends IColumn> Alignment<A, B> getAlignment() {
        return new AlignmentByName<>();
    }


    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(param.getJdbcUrl(), param.getUsername(), param.getPassword());
    }
}
