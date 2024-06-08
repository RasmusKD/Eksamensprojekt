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

public class CreateIngredientsTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @InjectMocks
    private DBRepository dBRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
    }

    @Test
    public void testCreateIngredients() throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Tomato");
        ingredient.setFat(0.2);
        ingredient.setCarbohydrate(3.9);
        ingredient.setProtein(0.9);
        ingredient.setCalories(18);

        dBRepository.createIngredients(ingredient);

        verify(preparedStatement).setString(1, "Tomato");
        verify(preparedStatement).setDouble(2, 0.2);
        verify(preparedStatement).setDouble(3, 3.9);
        verify(preparedStatement).setDouble(4, 0.9);
        verify(preparedStatement).setInt(5, 18);
        verify(preparedStatement).executeUpdate();
    }
}



/* 1: MockitoAnnotations.initMocks(this) initialiserer mocks
   2: Opretter en Ingredient-instans med testdata.
   3:Mock repository createIngredients metode til at gøre ingenting, da vi ikke har brug for den faktiske databaseinteraktion.
   4:Kald createIngredients-metoden i UseCase-klassen.
   5:Verificerer, at createIngredients-metoden i DBRepository blev kaldt præcis én gang med den oprettede ingrediens.
   6:Ekstra verificering for at sikre, at createIngredients-metoden blev kaldt med de korrekte parametre.
 */