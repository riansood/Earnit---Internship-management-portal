import dao.EmployeeDao;
import dao.StudentDao;
import model.Employee;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static javassist.runtime.DotClass.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeDaoTest {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = Mockito.mock(Connection.class);
        preparedStatement = Mockito.mock(PreparedStatement.class);
        resultSet = Mockito.mock(ResultSet.class);

        Mockito.when(connection.prepareStatement(
                ArgumentMatchers.anyString())).thenReturn(preparedStatement);

        // Use reflection to set the connection field in EmployeeDao.INSTANCE
        try {
            java.lang.reflect.Field field = EmployeeDao.class.getDeclaredField("connection");
            field.setAccessible(true);
            field.set(EmployeeDao.INSTANCE, connection);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMatchCredentials_Success() throws SQLException {
        String email = "test@example.com";
        String password = "password";
        String salt = "randomSalt";
        String hashedPassword = StudentDao.hashPassword(password, salt);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getString("password")).thenReturn(hashedPassword);
        Mockito.when(resultSet.getString("salt")).thenReturn(salt);
        Mockito.when(resultSet.getString("email")).thenReturn(email);

        Employee employee = EmployeeDao.INSTANCE.matchCredendials(email, password);

        Assertions.assertNotNull(employee);
        Assertions.assertEquals(email, employee.getEmail());
        Assertions.assertEquals(hashedPassword, employee.getPassword());
    }

    @Test
    public void testMatchCredentials_Failure_WrongPassword() throws SQLException {
        String email = "test@example.com";
        String password = "password";
        String wrongPassword = "wrongPassword";
        String salt = "randomSalt";
        String hashedPassword = StudentDao.hashPassword(password, salt);

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(true);
        Mockito.when(resultSet.getString("password")).thenReturn(hashedPassword);
        Mockito.when(resultSet.getString("salt")).thenReturn(salt);

        Employee employee = EmployeeDao.INSTANCE.matchCredendials(email, wrongPassword);

        Assertions.assertNull(employee);
    }

    @Test
    public void testMatchCredentials_Failure_NoSuchUser() throws SQLException {
        String email = "nonexistent@example.com";
        String password = "password";

        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(resultSet.next()).thenReturn(false);

        Employee employee = EmployeeDao.INSTANCE.matchCredendials(email, password);

        Assertions.assertNull(employee);
    }
}
