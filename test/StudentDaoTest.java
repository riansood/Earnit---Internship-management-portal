import dao.StudentDao;
import model.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class StudentDaoTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    private StudentDao studentDao;

    @BeforeEach
    public void setUp() throws SQLException, NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        // Inject the mock connection into the StudentDao.INSTANCE
        studentDao = StudentDao.INSTANCE;
        injectMockConnection(studentDao, mockConnection);
    }

    @AfterEach
    public void tearDown() {
        // Reset mocks after each test
        reset(mockConnection, mockStatement, mockResultSet);
    }

    private void injectMockConnection(StudentDao dao, Connection mockConnection) throws NoSuchFieldException, IllegalAccessException {
        Field connectionField = StudentDao.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        connectionField.set(dao, mockConnection);
    }

    @Test
    public void testCreateStudent() throws SQLException {
        Student student = new Student("John Doe", "john.doe@example.com", "123 Main St", "password", "1234567890", null);

        // Mock PreparedStatement behavior for create method
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1);

        // Call create method
        Student createdStudent = studentDao.create(student);

        // Verify interactions
        verify(mockConnection).prepareStatement(any(String.class));
        verify(mockStatement).setString(anyInt(), eq(student.getName()));
        verify(mockStatement).setString(anyInt(), eq(student.getEmail()));
        verify(mockStatement, times(6)).setString(anyInt(), anyString()); // Password hash, salt, etc.
        verify(mockStatement).executeUpdate();

        // Check if returned student matches expected
        assertNotNull(createdStudent);
        assertEquals(student.getName(), createdStudent.getName());
        assertEquals(student.getEmail(), createdStudent.getEmail());
        assertEquals(student.getPhoneNumber(), createdStudent.getPhoneNumber());
        assertEquals(student.getAddress(), createdStudent.getAddress());
    }

    @Test
    public void testGetStudentByEmail() throws SQLException {
        String email = "john.doe@example.com";
        String expectedName = "John Doe";
        String expectedPhoneNumber = "1234567890";
        String expectedAddress = "123 Main St";
        String expectedPasswordHash = "hashed_password";
        String expectedBTWNumber = null;

        // Mock ResultSet behavior for getStudentByEmail method
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulate result found
        when(mockResultSet.getString("name")).thenReturn(expectedName);
        when(mockResultSet.getString("email")).thenReturn(email);
        when(mockResultSet.getString("address")).thenReturn(expectedAddress);
        when(mockResultSet.getString("password")).thenReturn(expectedPasswordHash);
        when(mockResultSet.getString("phoneNumber")).thenReturn(expectedPhoneNumber);
        when(mockResultSet.getString("BTW_number")).thenReturn(expectedBTWNumber);

        // Call getStudentByEmail method
        Student retrievedStudent = studentDao.getStudentByEmail(email);

        // Verify interactions
        verify(mockConnection).prepareStatement(any(String.class));
        verify(mockStatement).setString(1, email);
        verify(mockStatement).executeQuery();
        verify(mockResultSet).next();
        verify(mockResultSet).getString("name");
        verify(mockResultSet).getString("email");
        verify(mockResultSet).getString("address");
        verify(mockResultSet).getString("password");
        verify(mockResultSet).getString("phoneNumber");
        verify(mockResultSet).getString("BTW_number");

        // Check if returned student matches expected
        assertNotNull(retrievedStudent);
        assertEquals(expectedName, retrievedStudent.getName());
        assertEquals(email, retrievedStudent.getEmail());
        assertEquals(expectedPhoneNumber, retrievedStudent.getPhoneNumber());
        assertEquals(expectedAddress, retrievedStudent.getAddress());
        assertEquals(expectedPasswordHash, retrievedStudent.getPassword());
        assertEquals(expectedBTWNumber, retrievedStudent.getBTW_number());
    }

    // Additional tests (e.g., for delete, updateBTWnumber, etc.) can be added here
}
