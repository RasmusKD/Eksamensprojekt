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
     * Sætter testmiljøet op før hver testmetode.
     *
     * @throws Exception hvis der opstår en fejl under opsætningen.
     */
    @Before
    public void setUp() throws Exception {
        // Initialiserer mock objekter
        MockitoAnnotations.initMocks(this);
        // Mock DataSource til at returnere mock Connection
        when(dataSource.getConnection()).thenReturn(connection);
        // Mock Connection til at returnere mock PreparedStatement
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    /**
     * Tester createIngredients metoden i DBRepository.
     *
     * @throws SQLException hvis en databaseadgangsfejl opstår.
     */
    @Test
    public void testCreateIngredients() throws SQLException {
        // Opretter en ny Ingredient objekt
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Tomato");
        ingredient.setFat(0.2);
        ingredient.setCarbohydrate(3.9);
        ingredient.setProtein(0.9);
        ingredient.setCalories(18);

        // Kalder metoden, der skal testes
        dBRepository.createIngredients(ingredient);

        // Verificerer at PreparedStatement blev kaldt med de korrekte parametre
        verify(preparedStatement).setString(1, "Tomato");
        verify(preparedStatement).setDouble(2, 0.2);
        verify(preparedStatement).setDouble(3, 3.9);
        verify(preparedStatement).setDouble(4, 0.9);
        verify(preparedStatement).setInt(5, 18);

        verify(preparedStatement).executeUpdate();
    }
}
