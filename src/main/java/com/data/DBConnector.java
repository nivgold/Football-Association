package com.data;


import com.logger.EventLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {
    public static final String URL = "jdbc:mysql://132.72.65.74:3306/footballassociationdb?useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String USER = "java";
    public static final String PASS = "password";

    public static Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
            EventLogger.getInstance().saveLog("new database connection has been created");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
        return connection;
    }
}
