package io.github.komyagin.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private ConnectionFactory() {}

    private static final String URL = "jdbc:sqlite:vocab.db";
    /**
     * Connect to the vocab.db database
     * @return the Connection object
     */
    public static Connection getConnection() {
        // SQLite connection string
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
}
