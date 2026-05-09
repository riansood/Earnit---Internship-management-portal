import dao.DatabaseConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DatabaseTest {

    private Connection connection;
    private Statement statement;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        statement = mock(Statement.class);

        when(connection.createStatement()).thenReturn(statement);
    }

    @Test
    public void testLoadDriver() {
        // No need to mock anything for loadDriver since it's just loading the driver.
        assertDoesNotThrow(DatabaseConnection::loadDriver);
    }

}
