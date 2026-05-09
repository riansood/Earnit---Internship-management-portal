/**
 * The {@code StudentDao} enum provides methods for managing student data and authentication.
 * <p>
 * This singleton class connects to the PostgreSQL database, performs CRUD operations on student data,
 * and handles password hashing and validation.
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import model.Student;


public enum StudentDao {
    INSTANCE;

    private Connection connection;

    /**
     * Private constructor for the singleton instance.
     * <p>
     * This constructor loads the PostgreSQL JDBC driver and establishes a connection to the database.
     * </p>
     */

    private StudentDao() {
        DatabaseConnection.loadDriver();
        this.connection = DatabaseConnection.getConnection();

    }

    /**
     * Generates a random salt for password hashing.
     *
     * @return a randomly generated salt as a Base64 encoded string
     */
    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);

    }
    /**
     * Hashes a password using SHA-256 with the provided salt.
     *
     * @param password the password to be hashed
     * @param salt the salt to be used in hashing
     * @return the hashed password as a Base64 encoded string
     */
    //use SHA-256 and salt and pepper for hashing
    public static String hashPassword(String password, String salt) {
        String saltedPassword = password + salt;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(saltedPassword.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Validates a password against the stored hashed password and salt.
     *
     * @param password the password to be validated
     * @param storedPassword the stored hashed password
     * @param storedSalt the stored salt
     * @return {@code true} if the password is valid; {@code false} otherwise
     */
    public boolean isPasswordValid(String password, String storedPassword, String storedSalt){
        String password1 = hashPassword(password, storedSalt);
        return password1.equals(storedPassword);
    }
    /**
     * Creates a new student record in the database.
     *
     * @param student the student object to be created
     * @return the created student object with hashed password and salt
     */
    public Student create(Student student) {
        String sql = "INSERT INTO student (name, email, password, salt, phoneNumber, address) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            String salt = getSalt();
            String hashedPassword = hashPassword(student.getPassword(), salt);

            statement.setString(1, student.getName());
            statement.setString(2, student.getEmail());
            statement.setString(3, hashedPassword);
            statement.setString(4, salt);
            statement.setString(5, student.getPhoneNumber());
            statement.setString(6, student.getAddress());
            statement.executeUpdate();
            return new Student(student.getName(),student.getEmail(),student.getAddress(), hashedPassword,student.getPhoneNumber(),null);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Retrieves a student by email from the database.
     *
     * @param email the email of the student to be retrieved
     * @return the retrieved student object; {@code null} if not found
     */
    public Student getStudentByEmail(String email) {
        String sql = "SELECT * FROM Student WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Student(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("address"),
                        resultSet.getString("password"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("BTW_number")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Retrieves the salt for a student by email from the database.
     *
     * @param email the email of the student whose salt is to be retrieved
     * @return the salt as a string
     * @throws RuntimeException if a database access error occurs
     */
    public String getSaltbyEmail(String email) {
        String sql = "SELECT salt FROM Student WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getString("salt");
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Student editProfile (Student student){
        String sql = " UPDATE student SET name = ?, address = ?, phonenumber = ? WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student.getName());
            statement.setString(2, student.getAddress());
            statement.setString(3, student.getPhoneNumber());
            statement.setString(4, student.getEmail());
            statement.executeUpdate();
            return student;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Deletes a student record from the database by email.
     *
     * @param email the email of the student to be deleted
     */
    public void delete(String email) {
        String deleteTaxSql = "DELETE FROM tax WHERE email = ?";
        String deleteEmploymentFormSql = "DELETE FROM employment_form WHERE student_id = ?";
        String deleteCommentSql = "DELETE FROM comment WHERE student_id = ?";
        String deleteStudentSql = "DELETE FROM student WHERE email = ?";

        try (PreparedStatement deleteTaxStmt = connection.prepareStatement(deleteTaxSql);
             PreparedStatement deleteEmploymentFormStmt = connection.prepareStatement(deleteEmploymentFormSql);
             PreparedStatement deleteCommentStmt = connection.prepareStatement(deleteCommentSql);
             PreparedStatement deleteStudentStmt = connection.prepareStatement(deleteStudentSql)) {

            // Delete from tax table
            deleteTaxStmt.setString(1, email);
            deleteTaxStmt.executeUpdate();

            // Delete from employment_form table
            deleteEmploymentFormStmt.setString(1, email);
            deleteEmploymentFormStmt.executeUpdate();

            // Delete from comment table
            deleteCommentStmt.setString(1, email);
            deleteCommentStmt.executeUpdate();

            // Delete from student table
            deleteStudentStmt.setString(1, email);
            deleteStudentStmt.executeUpdate();

            System.out.println("Successfully deleted student with email: " + email);

        } catch (SQLException e) {
            System.err.println("Error occurred while deleting student with email: " + email);
            e.printStackTrace();
        }
    }


    /**
     * Retrieves a list of all students from the database.
     *
     * @return a list of all student objects
     */
    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT s.email FROM student s";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                students.add(new Student(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("address"),
                        resultSet.getString("password"),
                        resultSet.getString("phoneNumber"),
                        resultSet.getString("BTW_number")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
    /**
     * Updates the BTW number for a student by email.
     *
     * @param email the email of the student whose BTW number is to be updated
     * @param newBTWnumber the new BTW number
     */
    public void updateBTWnumber(String email, String newBTWnumber){
        String sql = "UPDATE student SET BTW_number = ? WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newBTWnumber);
            statement.setString(2, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}