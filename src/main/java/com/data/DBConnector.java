package com.data;


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
            System.out.println("created new Database connection");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
        return connection;
    }
}
