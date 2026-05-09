/**
 * The {@code EmployeeDao} enum provides methods for handling employee authentication and database interactions.
 * <p>
 * This singleton class connects to the PostgreSQL database, retrieves employee credentials, and verifies them.
 * </p>
 */
package dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import model.Employee;
import model.Student;

public enum EmployeeDao {
    INSTANCE;

    private Connection connection;

    /**
     * Private constructor for the singleton instance.
     * <p>
     * This constructor loads the PostgreSQL JDBC driver and establishes a connection to the database.
     * </p>
     */

    private EmployeeDao() {
        DatabaseConnection.loadDriver();
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Matches the provided email and password against the stored credentials in the database.
     * <p>
     * This method retrieves the stored hashed password and salt for the given email,
     * hashes the provided password using the same salt, and compares it with the stored hashed password.
     * If the credentials match, it returns an {@code Employee} object.
     * </p>
     *
     * @param email the email of the employee
     * @param password the password of the employee
     * @return an {@code Employee} object if the credentials match; {@code null} otherwise
     * @throws SQLException if a database access error occurs
     */
    public Employee matchCredendials(String email, String password) throws SQLException {
        String sql = "SELECT * FROM employee WHERE email = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    String storedHashedPassword = resultSet.getString("password");
                    String storedSalt = resultSet.getString("salt");

                    String hashedPassword = StudentDao.hashPassword(password, storedSalt);

                    if (storedHashedPassword.equals(hashedPassword)) {
                        return new Employee(resultSet.getString("email"),
                                            resultSet.getString("password")); // Include salt in the returned Employee object
                    }
                }
            }
        }

        return null;
    }

}
