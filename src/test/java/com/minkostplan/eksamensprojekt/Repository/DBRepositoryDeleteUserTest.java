package com.minkostplan.eksamensprojekt.Repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

/*public class DBRepositoryDeleteUserTest {

    private DBRepository dbRepository;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        dbRepository = spy(DBRepository.class);

        // Mock getConnection to return our mockConnection
        doReturn(mockConnection).when(dbRepository).getConnection();
    }

    @Test
    public void testDeleteUserSuccess() throws SQLException {
        int userId = 1;

        // Mock prepareStatement to return our mockPreparedStatement
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);  // Simulate successful deletion

        boolean result = dbRepository.deleteUser(userId);

        assertTrue(result);
        verify(mockConnection).prepareStatement("DELETE FROM User WHERE userId = ?");
        verify(mockPreparedStatement).setInt(1, userId);
        verify(mockPreparedStatement).executeUpdate();
        verify(dbRepository).closeResources(mockConnection, mockPreparedStatement);
    }

    @Test
    public void testDeleteUserFailure() throws SQLException {
        int userId = 1;

        // Mock prepareStatement to return our mockPreparedStatement
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);  // Simulate failed deletion

        boolean result = dbRepository.deleteUser(userId);

        assertFalse(result);
        verify(mockConnection).prepareStatement("DELETE FROM User WHERE userId = ?");
        verify(mockPreparedStatement).setInt(1, userId);
        verify(mockPreparedStatement).executeUpdate();
        verify(dbRepository).closeResources(mockConnection, mockPreparedStatement);
    }

    @Test
    public void testDeleteUserSQLException() throws SQLException {
        int userId = 1;

        // Mock prepareStatement to throw an SQLException
        when(mockConnection.prepareStatement(any(String.class))).thenThrow(new SQLException("Test Exception"));

        boolean result = dbRepository.deleteUser(userId);

        assertFalse(result);
        verify(mockConnection).prepareStatement("DELETE FROM User WHERE userId = ?");
        verify(dbRepository).closeResources(mockConnection, null);
    }
}

//DENNE ER TILFØJET SÅ VI MÅSKE KAN HAVE EN DER IKKE VIRKER????
//SÅ KAN VI FORTÆLLE HVORFOR
//??

/*
setUp Metoden: Initialiserer mocks og spy til DBRepository. Metoden getConnection er mocked for at returnere en mock forbindelse (mockConnection).

testDeleteUserSuccess Metoden: Tester den succesfulde sletning af en bruger. Mock executeUpdate til at returnere 1 for at simulere succes. Bekræfter, at de relevante metoder blev kaldt.

testDeleteUserFailure Metoden: Tester mislykket sletning af en bruger. Mock executeUpdate til at returnere 0 for at simulere fejl. Bekræfter, at de relevante metoder blev kaldt.

testDeleteUserSQLException Metoden: Tester scenariet hvor SQLException kastes. Mock prepareStatement til at kaste en SQLException. Bekræfter, at metoderne til ressource lukning blev kaldt korrekt.

 */