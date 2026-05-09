
import dao.TaxDao;
import model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaxDaoTest {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        // Use reflection to set the connection field in TaxDao.INSTANCE
        try {
            java.lang.reflect.Field field = TaxDao.class.getDeclaredField("connection");
            field.setAccessible(true);
            field.set(TaxDao.INSTANCE, connection);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail("Failed to set connection field in TaxDao.INSTANCE");
        }
    }

    @Test
    public void testGetMaxId_Success() throws SQLException {
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(10);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        int maxId = TaxDao.INSTANCE.getMaxId();

        assertEquals(10, maxId);
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetMaxId_NoRecords() throws SQLException {
        when(resultSet.next()).thenReturn(false);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        int maxId = TaxDao.INSTANCE.getMaxId();

        assertEquals(0, maxId);
        verify(preparedStatement, times(1)).executeQuery();
    }

    
    @Test
    public void testGetTaxByEmail_Success() throws SQLException {
        String email = "test@example.com";

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getDouble("taxAmount")).thenReturn(210.0);
        when(resultSet.getBoolean("isPaid")).thenReturn(false);
        when(resultSet.getString("email")).thenReturn(email);
        when(resultSet.getString("date")).thenReturn("29/06/2023");
        when(resultSet.getDouble("earning")).thenReturn(1000.0);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Tax tax = TaxDao.INSTANCE.getTaxByEmail(email);

        assertNotNull(tax);
        assertEquals(email, tax.getEmail());
        verify(preparedStatement, times(1)).executeQuery();
    }

    @Test
    public void testGetTaxByEmail_NoSuchUser() throws SQLException {
        String email = "nonexistent@example.com";

        when(resultSet.next()).thenReturn(false);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        Tax tax = TaxDao.INSTANCE.getTaxByEmail(email);

        assertNull(tax);
        verify(preparedStatement, times(1)).executeQuery();
    }



}
