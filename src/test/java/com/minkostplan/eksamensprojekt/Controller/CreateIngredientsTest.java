package com.minkostplan.eksamensprojekt.Controller;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;

/**
 * Unit test for DBRepository class.
 */
public class CreateIngredientsTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @InjectMocks
    private DBRepository dBRepository;

    /**
     * Sets up the test environment before each test method.
     *
     * @throws Exception if an error occurs during setup.
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    /**
     * Tests the createIngredients method of DBRepository.
     *
     * @throws SQLException if a database access error occurs.
     */
    @Test
    public void testCreateIngredients() throws SQLException {
        // Create a new Ingredient object
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Tomato");
        ingredient.setFat(0.2);
        ingredient.setCarbohydrate(3.9);
        ingredient.setProtein(0.9);
        ingredient.setCalories(18);

        // Call the method to test
        dBRepository.createIngredients(ingredient);

        // Verify that the PreparedStatement was called with the correct parameters
        verify(preparedStatement).setString(1, "Tomato");
        verify(preparedStatement).setDouble(2, 0.2);
        verify(preparedStatement).setDouble(3, 3.9);
        verify(preparedStatement).setDouble(4, 0.9);
        verify(preparedStatement).setInt(5, 18);
        verify(preparedStatement).executeUpdate();
    }
}
