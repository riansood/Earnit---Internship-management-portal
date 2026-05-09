package dao;

import java.sql.*;
import model.Employee;
import model.Student;

public class DatabaseConnection {
    private static String host = "bronto.ewi.utwente.nl";
    private static String dbName = "dab_di23242b_133";
    private static String url = "jdbc:postgresql://" + host + ":5432/" + dbName;
    private static String username = "dab_di23242b_133";
    private static String password = "snCN+GM/3fkdyUkv";
    /**
     * Loads the PostgreSQL JDBC driver.
     * <p>
     * This method attempts to load the driver class for PostgreSQL database connectivity.
     * If the driver class is not found, it prints an error message to the standard error stream.
     * </p>
     */
    public static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Error loading driver: " + cnfe);
        }
    }

    /**
     * Establishes and returns a connection to the PostgreSQL database.
     * <p>
     * This method uses the JDBC {@code DriverManager} to obtain a connection to the specified database
     * using the provided URL, username, and password.
     * If the connection fails, it prints an error message to the standard error stream.
     * </p>
     *
     * @return a {@code Connection} object if the connection is successful; {@code null} otherwise
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            System.out.println(url);
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException sqle) {
            System.err.println("Error connecting: " + sqle);
        }
        return connection;
    }

    /**
     * The main method that executes SQL statements to modify the database schema.
     * <p>
     * This method first loads the PostgreSQL JDBC driver and then attempts to establish a connection to the database.
     * If the connection is successful, it creates a {@code Statement} object and executes SQL statements
     * to alter the "Earnings" table by adding new columns.
     * </p>
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        loadDriver();
        try (Connection connection = getConnection()) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                statement.execute("ALTER TABLE Earnings ADD VAT FLOAT;");
                statement.execute("ALTER TABLE Earnings ADD personalExpVAT FLOAT;");
            } else {
                System.err.println("Failed to establish connection.");
            }
        } catch (SQLException sqle) {
            System.err.println("SQL error: " + sqle);
        }
    }
}