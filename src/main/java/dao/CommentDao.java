package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Comment;
import model.Student;

/**
 * Enum representing the Data Access Object (DAO) for Comment operations.
 * It provides methods to perform CRUD operations on comments in the database.
 */
public enum CommentDao {

    INSTANCE;

    private Connection connection;

    /**
     * Private constructor to initialize the database connection.
     * Loads the database driver and establishes a connection.
     */
    CommentDao() {
        DatabaseConnection.loadDriver();
        this.connection = DatabaseConnection.getConnection();
    }

    /**
     * Creates a new comment in the database.
     *
     * @param comment the Comment object to be created.
     * @return the created Comment object with updated fields, or null if the creation fails.
     */
    public Comment createComment(Comment comment) {
        String sql = "INSERT INTO comment (comment_id, message, isaccepted, student_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, String.valueOf(getMaxId() + 1));
            statement.setString(2, comment.getMessage());
            statement.setBoolean(3, false);
            statement.setString(4, comment.getStudent_id());
            statement.executeUpdate();
            return new Comment(comment.getComment_id(), comment.getMessage(), comment.isAccepted(), comment.getStudent_id());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates the acceptance status of comments for a given student.
     *
     * @param student_id the ID of the student whose comments' acceptance status is to be updated.
     */
    public void update_isaccepted(String student_id) {
        String sql = "UPDATE comment SET isaccepted = true WHERE student_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student_id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isApproved(String student_id){
        String sql = "SELECT isaccepted FROM comment WHERE student_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student_id);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                return resultSet.getBoolean("isaccepted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Retrieves the comments for a given student.
     *
     * @param student_id the ID of the student whose comments are to be retrieved.
     * @return a list of comment messages for the given student.
     */
    public List<String> getComment(String student_id) {
        String sql = "SELECT message FROM Comment WHERE student_id = ? ORDER BY comment_id DESC";

        List<String> comments = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student_id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                comments.add(resultSet.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }

    /**
     * Retrieves all comments.
     *
     * @return a list of all comment messages.
     */
    public List<String> getComments() {
        String sql = "SELECT message FROM Comment ORDER BY comment_id DESC";

        List<String> comments = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                comments.add(resultSet.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }

    /**
     * Retrieves the maximum comment ID currently in the database.
     *
     * @return the maximum comment ID, or 0 if there are no comments.
     * @throws SQLException if a database access error occurs.
     */
    public int getMaxId() throws SQLException {
        String query = "SELECT comment_id FROM comment ORDER BY comment_id DESC LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("comment_id");
            } else {
                return 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
