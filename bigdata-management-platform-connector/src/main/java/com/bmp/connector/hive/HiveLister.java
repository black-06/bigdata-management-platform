package com.bmp.connector.hive;

import com.bmp.commons.enums.AssetType;
import com.bmp.connector.api.list.AssetPath;
import com.bmp.connector.api.list.Lister;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class HiveLister implements Lister {
    public static final String SEP = ".";
    public static final String QUOTE = "`";

    private final Connection connection;

    public HiveLister(final HiveConnectorInfo info) {
        this.connection = HiveUtils.open(info);
    }

    @Override
    public List<Lister.Asset> list(AssetPath path) throws SQLException {
        if (path == null) {
            path = new AssetPath();
        }
        switch (path.getPaths().length) {
            case 0: // it's root path
                List<Lister.Asset> tables = new ArrayList<>();
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

    private List<Lister.Asset> listDatabases(AssetPath root) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            List<Lister.Asset> assets = new ArrayList<>();
            ResultSet rs = statement.executeQuery("SHOW DATABASES ");
            while (rs.next()) {
                AssetPath database = root.getChild(rs.getString(1));
                assets.add(descDatabase(database));
            }
            return assets;
        }
    }

    private Lister.Asset descDatabase(AssetPath database) {
        return new Lister.Asset()
                .setName(database.getName())
                .setType(AssetType.DATABASE)
                .setPath(database);
    }

    private List<Lister.Asset> listTables(Asset database) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            List<Lister.Asset> tables = new ArrayList<>();
            ResultSet rs = statement.executeQuery(String.format("SHOW TABLES IN %s", database.getPath().getFullName(SEP, QUOTE, QUOTE)));
            while (rs.next()) {
                AssetPath table = database.getPath().getChild(rs.getString(1));
                tables.add(descTable(database, table));
            }
            return tables;
        }
    }

    private Lister.Asset descTable(Asset database, AssetPath table) throws SQLException {
        return new Lister.Asset()
                .setName(table.getName())
                .setType(AssetType.TABLE)
                .setParent(database)
                .setPath(table)
                .setColumns(listColumns(table));
    }

    private List<Lister.Column> listColumns(AssetPath table) throws SQLException {
        String fullName = table.getFullName(SEP, QUOTE, QUOTE);
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(String.format("DESCRIBE %s", fullName));
            Map<String, Lister.Column> map = new HashMap<>();
            while (rs.next()) {
                Column column = new Column()
                        .setField(rs.getString(1))
                        .setType(rs.getString(2))
                        .setExtra(rs.getString(3));
                map.put(column.getField(), column);
            }
            List<Lister.Column> columns = new ArrayList<>();
            rs = statement.executeQuery(String.format("SHOW COLUMNS IN %s", fullName));
            while (rs.next()) {
                Lister.Column column = map.get(rs.getString(1));
                if (column != null) {
                    columns.add(column);
                }
            }
            return columns;
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
