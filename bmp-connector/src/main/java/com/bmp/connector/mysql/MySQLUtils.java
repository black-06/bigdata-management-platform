package com.bmp.connector.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLUtils {

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
}
