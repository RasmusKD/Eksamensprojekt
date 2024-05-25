package com.minkostplan.eksamensprojekt.Repository;

import com.minkostplan.eksamensprojekt.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

/*public class DBRepositoryCreateUserTest {

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
    public void testCreateUserSuccess() throws SQLException {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setAge(30);
        user.setGender('M');
        user.setWeight(70.0);
        user.setHeight(175.0);
        user.setActivityLevel(3);
        user.setGoal(1);
        user.setEmployed(1);
        user.setSubscriber(true);

        // Mock prepareStatement to return our mockPreparedStatement
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);

        dbRepository.createUser(user);

        verify(mockConnection).prepareStatement("INSERT INTO User (firstName, lastName, email, password, age, gender, weight, height, activityLevel, goal, employed, subscriber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        verify(mockPreparedStatement).setString(1, user.getFirstName());
        verify(mockPreparedStatement).setString(2, user.getLastName());
        verify(mockPreparedStatement).setString(3, user.getEmail());
        verify(mockPreparedStatement).setString(4, user.getPassword());
        verify(mockPreparedStatement).setInt(5, user.getAge());
        verify(mockPreparedStatement).setString(6, String.valueOf(user.getGender()));
        verify(mockPreparedStatement).setDouble(7, user.getWeight());
        verify(mockPreparedStatement).setDouble(8, user.getHeight());
        verify(mockPreparedStatement).setInt(9, user.getActivityLevel());
        verify(mockPreparedStatement).setInt(10, user.getGoal());
        verify(mockPreparedStatement).setInt(11, user.getEmployed());
        verify(mockPreparedStatement).setBoolean(12, user.isSubscriber());
        verify(mockPreparedStatement).executeUpdate();
        verify(dbRepository).closeResources(mockConnection, mockPreparedStatement);
    }

    @Test
    public void testCreateUserSQLException() throws SQLException {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setAge(30);
        user.setGender('M');
        user.setWeight(70.0);
        user.setHeight(175.0);
        user.setActivityLevel(3);
        user.setGoal(1);
        user.setEmployed(1);
        user.setSubscriber(true);

        // Mock prepareStatement to throw an SQLException
        when(mockConnection.prepareStatement(any(String.class))).thenThrow(new SQLException("Test Exception"));

        dbRepository.createUser(user);

        verify(mockConnection).prepareStatement("INSERT INTO User (firstName, lastName, email, password, age, gender, weight, height, activityLevel, goal, employed, subscriber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        verify(dbRepository).closeResources(mockConnection, null);
    }
}

/*
Forklaring

    setUp Metoden: Initialiserer mocks og spy til DBRepository. Metoden getConnection er mocked for at returnere en mock forbindelse (mockConnection).
    testCreateUserSuccess Metoden: Tester den succesfulde oprettelse af en bruger. Bekræfter, at de relevante metoder blev kaldt med de korrekte argumenter, og at executeUpdate blev kaldt.
    testCreateUserSQLException Metoden: Tester scenariet hvor SQLException kastes. Mock prepareStatement til at kaste en SQLException. Bekræfter, at metoderne til ressource lukning blev kaldt korrekt.
 */