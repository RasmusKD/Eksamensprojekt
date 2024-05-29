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

/**
 * Testklasse for sletning af brugere i DBRepository.
 */
public class DBRepositoryDeleteUserTest {

    private DBRepository dbRepository;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    /**
     * Opsætning af tests. Initialiserer mocks og spy til DBRepository.
     *
     * @throws SQLException hvis der opstår en fejl under opsætning af forbindelse.
     */
    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        dbRepository = spy(DBRepository.class);

        // Mock getConnection til at returnere mockConnection
        doReturn(mockConnection).when(dbRepository).getConnection();
    }

    /**
     * Test for at sikre, at en bruger kan slettes korrekt fra databasen.
     *
     * @throws SQLException hvis der opstår en fejl under SQL-udførelse.
     */
    @Test
    public void testDeleteUserSuccess() throws SQLException {
        int userId = 1;

        // Mock prepareStatement til at returnere mockPreparedStatement
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);  // Simulerer succesfuld sletning

        boolean result = dbRepository.deleteUser(userId);

        assertTrue(result);
        verify(mockConnection).prepareStatement("DELETE FROM User WHERE userId = ?");
        verify(mockPreparedStatement).setInt(1, userId);
        verify(mockPreparedStatement).executeUpdate();
        verify(dbRepository).closeResources(mockConnection, mockPreparedStatement);
    }

    /**
     * Test for at sikre, at en fejlet sletning håndteres korrekt.
     *
     * @throws SQLException hvis der opstår en fejl under SQL-udførelse.
     */
    @Test
    public void testDeleteUserFailure() throws SQLException {
        int userId = 1;

        // Mock prepareStatement til at returnere mockPreparedStatement
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);  // Simulerer fejlet sletning

        boolean result = dbRepository.deleteUser(userId);

        assertFalse(result);
        verify(mockConnection).prepareStatement("DELETE FROM User WHERE userId = ?");
        verify(mockPreparedStatement).setInt(1, userId);
        verify(mockPreparedStatement).executeUpdate();
        verify(dbRepository).closeResources(mockConnection, mockPreparedStatement);
    }

    /**
     * Test for at sikre, at SQLException håndteres korrekt ved sletning af bruger.
     *
     * @throws SQLException hvis der opstår en fejl under SQL-udførelse.
     */
    @Test
    public void testDeleteUserSQLException() throws SQLException {
        int userId = 1;

        // Mock prepareStatement til at kaste en SQLException
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