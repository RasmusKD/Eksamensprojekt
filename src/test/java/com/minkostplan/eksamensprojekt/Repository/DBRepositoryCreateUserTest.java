package com.minkostplan.eksamensprojekt.Repository;

import com.minkostplan.eksamensprojekt.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

/**
 * Testklasse for oprettelse af brugere i DBRepository.
 */
//vores testklasse
public class DBRepositoryCreateUserTest {


    //Deklarerer en instansvariabel dbRepository af typen DBRepository.
    private DBRepository dbRepository;



    //Mock objekt er en falsk version af et rigtigt objekt, som man kan bruge til at simulere med

    //Deklarerer en mock af Connection-objektet ved hjælp af Mockito's @Mock-annotering.
    @Mock
    private Connection mockConnection;

    //Deklarerer en mock af PreparedStatement-objektet ved hjælp af Mockito's @Mock-annotering.
    @Mock
    private PreparedStatement mockPreparedStatement;

    /**
     * Opsætning af tests. Initialiserer mocks og spy til DBRepository.
     *
     * @throws SQLException hvis der opstår en fejl under opsætning af forbindelse.
     */
    //Annoterer setUp-metoden med @BeforeEach, hvilket betyder, at denne metode køres før hver testmetode i denne klasse.
    @BeforeEach
    public void setUp() throws SQLException {

        //Initialiserer mock-objekterne i denne testklasse. Dette fortæller Mockito at behandle de felter, der er annoteret med @Mock.
        MockitoAnnotations.initMocks(this);

        //Opretter en spy-instans af DBRepository. En spy giver mulighed for at overvåge og mocke DBRepository.
        dbRepository = spy(DBRepository.class);

        // Mock getConnection til at returnere mockConnection
        doReturn(mockConnection).when(dbRepository).getConnection();
    }

    /**
     * Test for at sikre, at en bruger kan oprettes korrekt i databasen.
     *
     * @throws SQLException hvis der opstår en fejl under SQL-udførelse.
     */
    //denne annotation viser at det er en test
    @Test
    public void testCreateUserSuccess() throws SQLException {
        User user = new User(); //Opretter en ny instans af User.
        user.setFirstName("Erik");//Sætter brugerens fornavn osv...
        user.setLastName("Knudsen");
        user.setEmail("erik@testmail.dk");
        user.setPassword("password");
        user.setBirthday(LocalDate.of(2000, 1, 2));
        user.setGender('M');
        user.setWeight(70.0);
        user.setHeight(175.0);
        user.setActivityLevel(3);
        user.setGoal(1);
        user.setEmployed(1);
        user.setSubscriber(true);

       // Mocker prepareStatement-metoden i mockConnection til at returnere mockPreparedStatement, uanset hvilken SQL-streng der gives som argument.
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockPreparedStatement);

        //Kalder createUser-metoden på dbRepository med user som argument.
        dbRepository.createUser(user);

        //Verificerer, at prepareStatement blev kaldt med den korrekte SQL-indsættelsesforespørgsel.
        verify(mockConnection).prepareStatement("INSERT INTO User (firstName, lastName, email, password, birthday, gender, weight, height, activityLevel, goal, employed, subscriber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        verify(mockPreparedStatement).setString(1, user.getFirstName());//Verificerer, at setString på mockPreparedStatement blev kaldt med den første parameter (1) sat til brugerens fornavn.
        verify(mockPreparedStatement).setString(2, user.getLastName()); //Verificerer, at setString på mockPreparedStatement blev kaldt med den anden parameter (2) sat til brugerens efternavn.
        verify(mockPreparedStatement).setString(3, user.getEmail());
        verify(mockPreparedStatement).setString(4, user.getPassword());
        verify(mockPreparedStatement).setDate(5, java.sql.Date.valueOf(user.getBirthday()));
        verify(mockPreparedStatement).setString(6, String.valueOf(user.getGender()));
        verify(mockPreparedStatement).setDouble(7, user.getWeight());
        verify(mockPreparedStatement).setDouble(8, user.getHeight());
        verify(mockPreparedStatement).setInt(9, user.getActivityLevel());
        verify(mockPreparedStatement).setInt(10, user.getGoal());
        verify(mockPreparedStatement).setInt(11, user.getEmployed());
        verify(mockPreparedStatement).setBoolean(12, user.isSubscriber());
        verify(mockPreparedStatement).executeUpdate();//Verificerer, at executeUpdate på mockPreparedStatement blev kaldt for at udføre SQL-forespørgslen.
        verify(dbRepository).closeResources(mockConnection, mockPreparedStatement);
        //Verificerer, at closeResources-metoden på dbRepository blev kaldt med mockConnection og mockPreparedStatement som argumenter for at lukke ressourcerne.
    }

    /**
     * Test for at sikre, at SQLException håndteres korrekt ved oprettelse af bruger.
     *
     * @throws SQLException hvis der opstår en fejl under SQL-udførelse.
     */
    @Test
    public void testCreateUserSQLException() throws SQLException {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setBirthday(LocalDate.of(1996, 5, 2));
        user.setGender('M');
        user.setWeight(70.0);
        user.setHeight(175.0);
        user.setActivityLevel(3);
        user.setGoal(1);
        user.setEmployed(1);
        user.setSubscriber(true);

        // Mock prepareStatement til at kaste en SQLException
        when(mockConnection.prepareStatement(any(String.class))).thenThrow(new SQLException("Test Exception"));

        dbRepository.createUser(user);

        verify(mockConnection).prepareStatement("INSERT INTO User (firstName, lastName, email, password, birthday, gender, weight, height, activityLevel, goal, employed, subscriber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        verify(dbRepository).closeResources(mockConnection, null);
    }
}

/*
Forklaring

    setUp Metoden: Initialiserer mocks og spy til DBRepository. Metoden getConnection er mocked for at returnere en mock forbindelse (mockConnection).
    testCreateUserSuccess Metoden: Tester den succesfulde oprettelse af en bruger. Bekræfter, at de relevante metoder blev kaldt med de korrekte argumenter, og at executeUpdate blev kaldt.
    testCreateUserSQLException Metoden: Tester scenariet hvor SQLException kastes. Mock prepareStatement til at kaste en SQLException. Bekræfter, at metoderne til ressource lukning blev kaldt korrekt.
 */
