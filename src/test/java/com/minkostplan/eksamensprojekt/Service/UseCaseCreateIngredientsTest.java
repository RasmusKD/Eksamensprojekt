package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

/**
 * Testklasse for oprettelse af ingredienser i UseCase-klassen.
 */
public class UseCaseCreateIngredientsTest {

    @InjectMocks
    private UseCase useCase;

    @Mock
    private DBRepository dbRepository;

    /**
     * Opsætning af tests. Initialiserer mocks og inject mocks i UseCase.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test for at sikre, at en ingrediens kan oprettes korrekt i databasen.
     */
    @Test
    public void testCreateIngredients() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Test Ingredient");
        ingredient.setFat(10.0);
        ingredient.setCarbohydrate(20.0);
        ingredient.setProtein(30.0);
        ingredient.setCalories(100);

        doNothing().when(dbRepository).createIngredients(ingredient);

        useCase.createIngredients(ingredient);

        verify(dbRepository, times(1)).createIngredients(ingredient);
        // Verify that the method was called with the correct parameter
        verify(dbRepository).createIngredients(argThat(arg ->
                arg.getName().equals("Test Ingredient") &&
                        arg.getFat() == 10.0 &&
                        arg.getCarbohydrate() == 20.0 &&
                        arg.getProtein() == 30.0 &&
                        arg.getCalories() == 100
        ));
    }
}


/* 1: MockitoAnnotations.initMocks(this) initialiserer mocks
   2: Opretter en Ingredient-instans med testdata.
   3:Mock repository createIngredients metode til at gøre ingenting, da vi ikke har brug for den faktiske databaseinteraktion.
   4:Kald createIngredients-metoden i UseCase-klassen.
   5:Verificerer, at createIngredients-metoden i DBRepository blev kaldt præcis én gang med den oprettede ingrediens.
   6:Ekstra verificering for at sikre, at createIngredients-metoden blev kaldt med de korrekte parametre.
 */