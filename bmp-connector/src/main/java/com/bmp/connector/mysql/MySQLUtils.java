package com.bmp.connector.mysql;

import com.bmp.connector.api.alignment.IColumn;
import com.bmp.connector.api.list.AssetPath;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class MySQLUtils {
    public static final String SEP = ".";
    public static final String QUOTE = "`";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection open(MySQLConnectorInfo info) {
        try {
            Connection connection = DriverManager.getConnection(info.getJdbcUrl(), info.getUsername(), info.getPassword());
            if (connection == null) {
                throw new IllegalArgumentException("test connection failed");
            }
            return connection;
        } catch (SQLException e) {
            throw new IllegalArgumentException("test connection failed", e);
        }
    }

    public static <C extends IColumn> String buildQuery(AssetPath path, List<C> columns, int limit) {
        if (limit > 0) {
            return String.format(
                    "SELECT %s FROM %s LIMIT %d", buildSelect(columns), path.getFullName(SEP, QUOTE, QUOTE), limit
            );
        }
        return String.format(
                "SELECT %s FROM %s", buildSelect(columns), path.getFullName(SEP, QUOTE, QUOTE)
        );
    }

    private static <C extends IColumn> String buildSelect(List<C> columns) {
        StringBuilder builder = new StringBuilder();
        boolean sep = false;
        for (IColumn column : columns) {
            if (sep) {
                builder.append(", ");
            } else {
                sep = true;
            }
            builder.append(MySQLUtils.QUOTE);
            builder.append(column.getName());
            builder.append(MySQLUtils.QUOTE);
        }
        return builder.toString();
    }
}
