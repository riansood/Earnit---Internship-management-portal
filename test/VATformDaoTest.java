

import dao.VATformDao;
import java.io.IOException;
import model.VATform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VATformDaoTest {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        // Use reflection to set the connection field in VATformDao.INSTANCE
        try {
            java.lang.reflect.Field field = VATformDao.class.getDeclaredField("connection");
            field.setAccessible(true);
            field.set(VATformDao.INSTANCE, connection);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail("Failed to set connection field in VATformDao.INSTANCE");
        }
    }

    @Test
    public void testCreateVATform_Success() throws SQLException, IOException {
        VATform form = new VATform(1, "student1@example.com", "Company", "Address", "2023-06-29",
                                   "1234567890", "www.company.com", "Student Name", "Student Address", "2000-01-01",
                                   "123456789", "Nature of Business", 100000.0, 50000.0, "10", "Initials Surname",
                                   "2023-06-30", "0987654321", "signatureBase64");

        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("form_id")).thenReturn(1);

        VATform createdForm = VATformDao.INSTANCE.create(form);

        assertNotNull(createdForm);
        assertEquals(form.getStudent_id(), createdForm.getStudent_id());
        verify(preparedStatement, times(1)).executeUpdate();
    }

    @Test
    public void testGetMaxId_Success() throws SQLException {
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("form_id")).thenReturn(10);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        int maxId = VATformDao.INSTANCE.getMaxId();

        assertEquals(10, maxId);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetMaxId_NoRecords() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        int maxId = VATformDao.INSTANCE.getMaxId();

        assertEquals(0, maxId);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetVATform_Success() throws SQLException {
        String email = "student1@example.com";

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("form_id")).thenReturn(1);
        when(resultSet.getString("student_id")).thenReturn(email);
        // Mock other fields similarly...
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        VATform form = VATformDao.INSTANCE.getVATform(email);

        assertNotNull(form);
        assertEquals(email, form.getStudent_id());
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetVATform_NoSuchUser() throws SQLException {
        String email = "nonexistent@example.com";

        when(resultSet.next()).thenReturn(false);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        VATform form = VATformDao.INSTANCE.getVATform(email);

        assertNull(form);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetStudents_Success() throws SQLException {
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("student_id")).thenReturn("student1@example.com");
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        List<String> students = VATformDao.INSTANCE.getStudents();

        assertNotNull(students);
        assertEquals(1, students.size());
        assertEquals("student1@example.com", students.get(0));
        verify(preparedStatement, times(1)).executeQuery();
    }
}
