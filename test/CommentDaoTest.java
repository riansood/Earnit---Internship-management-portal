
import dao.CommentDao;
import model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CommentDaoTest {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        // Use reflection to set the connection field in CommentDao.INSTANCE
        try {
            java.lang.reflect.Field field = CommentDao.class.getDeclaredField("connection");
            field.setAccessible(true);
            field.set(CommentDao.INSTANCE, connection);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testUpdate_isaccepted_Success() throws SQLException {
        String studentId = "student1";

        when(preparedStatement.executeUpdate()).thenReturn(1);

        CommentDao.INSTANCE.update_isaccepted(studentId);

        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testGetComment_Success() throws SQLException {
        String studentId = "student1";
        List<String> expectedComments = new ArrayList<>();
        expectedComments.add("Comment 1");
        expectedComments.add("Comment 2");

        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString("message")).thenReturn("Comment 2", "Comment 1");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        List<String> retrievedComments = CommentDao.INSTANCE.getComment(studentId);

        assertEquals(expectedComments.size(), retrievedComments.size());
        assertEquals(expectedComments, retrievedComments);
        verify(preparedStatement, times(1)).executeQuery();
    }


    @Test
    public void testGetMaxId_Success() throws SQLException {
        int expectedMaxId = 2;

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("comment_id")).thenReturn(2);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        int maxId = CommentDao.INSTANCE.getMaxId();

        assertEquals(expectedMaxId, maxId);
        verify(preparedStatement, times(1)).executeQuery();
    }
}
