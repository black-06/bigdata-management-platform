package com.bmp.connector.hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HiveUtils {

    static {
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection open(HiveConnectorInfo info) {
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
