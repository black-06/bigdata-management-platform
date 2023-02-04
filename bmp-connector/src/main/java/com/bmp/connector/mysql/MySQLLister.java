package com.bmp.connector.mysql;

import com.bmp.commons.enums.AssetType;
import com.bmp.connector.api.list.AssetPath;
import com.bmp.connector.api.list.Lister;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MySQLLister implements Lister {
    public static final String SEP = ".";
    public static final String QUOTE = "`";

    private final Connection connection;

    public MySQLLister(final MySQLConnectorInfo info) {
        this.connection = MySQLUtils.open(info);
    }

    @Override
    public List<Asset> list(AssetPath path) throws SQLException {
        if (path == null) {
            path = new AssetPath();
        }
        switch (path.getPaths().length) {
            case 0: // it's root path
                List<Asset> tables = new ArrayList<>();
                for (Asset database : listDatabases(path)) {
                    tables.addAll(listTables(database));
                }
                return tables;
            case 1: // it's a database path
                return listTables(descDatabase(path));
            case 2: // it's a table path
                return Collections.singletonList(descTable(descDatabase(path.getParent()), path));
            default:
                throw new IllegalArgumentException(String.format("unknown asset path %s", path.getFullName(SEP)));
        }
    }

    @Override
    public String buildPath(AssetPath path, boolean quote) {
        if (quote) {
            return path.getFullName(SEP, QUOTE, QUOTE);
        }
        return path.getFullName(SEP);
    }

    private List<Asset> listDatabases(AssetPath root) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            List<Asset> assets = new ArrayList<>();
            ResultSet rs = statement.executeQuery("SHOW DATABASES");
            while (rs.next()) {
                AssetPath database = root.getChild(rs.getString(1));
                assets.add(descDatabase(database));
            }
            return assets;
        }
    }

    private Asset descDatabase(AssetPath database) {
        return new Asset()
                .setName(database.getName())
                .setType(AssetType.DATABASE)
                .setPath(database);
    }

    private List<Asset> listTables(Asset database) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            List<Asset> tables = new ArrayList<>();
            ResultSet rs = statement.executeQuery(String.format("SHOW TABLES IN %s", database.getPath().getFullName(SEP, QUOTE, QUOTE)));
            while (rs.next()) {
                AssetPath table = database.getPath().getChild(rs.getString(1));
                tables.add(descTable(database, table));
            }
            return tables;
        }
    }

    private Asset descTable(Asset database, AssetPath table) throws SQLException {
        return new Asset()
                .setName(table.getName())
                .setType(AssetType.TABLE)
                .setParent(database)
                .setPath(table)
                .setColumns(listColumns(table));
    }

    private List<Column> listColumns(AssetPath table) throws SQLException {
        String fullName = table.getFullName(SEP, QUOTE, QUOTE);
        try (Statement statement = connection.createStatement()) {
            List<Column> columns = new ArrayList<>();
            ResultSet rs = statement.executeQuery(String.format("SHOW COLUMNS IN %s", fullName));
            while (rs.next()) {
                columns.add(new Column()
                        .setField(rs.getString(1))
                        .setType(rs.getString(2))
                        .setNull(rs.getString(3))
                        .setDefault(rs.getString(5))
                        .setExtra(rs.getString(6)));
            }
            return columns;
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
