package com.caesolutions.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // These constants should match the PHP equivalent:
    // host = "170.80.140.2", port="6161", db_name = "CAECLIENTES", username = "ORIONSYS", password = "123"
    private static final String URL = "jdbc:sqlserver://170.80.140.2:6161;databaseName=CAECLIENTES;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "ORIONSYS";
    private static final String PASSWORD = "123";

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Ensure the driver is loaded (optional in newer JDBC, but good practice)
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Base de datos conectada exitosamente.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar conexión: " + e.getMessage());
        }
    }
}
