package com.minkostplan.eksamensprojekt;

import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Testklasse for RecipeUseCase.
 */
public class RecipeUseCaseTest {

    @Mock
    private DBRepository dBRepository;

    @InjectMocks
    private UseCase useCase;

    /**
     * Initialiserer mocks før hver test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tester getAllRecipes-metoden i UseCase-klassen.
     */
    @Test
    public void testGetAllRecipes() {
        // Opret en liste med dummy opskrifter
        Recipe recipe1 = new Recipe();
        recipe1.setRecipeId(1);
        recipe1.setTitle("Recipe1");
        recipe1.setDescription("Description1");
        recipe1.setMethod("Method1");
        recipe1.setCookingTime("30 mins");
        recipe1.setImageUrl("http://example.com/image1.jpg");
        recipe1.setMealTime("Breakfast");
        recipe1.setTotalCalories(300);
        recipe1.setTotalProtein(20);
        recipe1.setTotalFat(10);
        recipe1.setTotalCarbohydrates(40);
        recipe1.setWeek("2024-W23");

        Recipe recipe2 = new Recipe();
        recipe2.setRecipeId(2);
        recipe2.setTitle("Recipe2");
        recipe2.setDescription("Description2");
        recipe2.setMethod("Method2");
        recipe2.setCookingTime("45 mins");
        recipe2.setImageUrl("http://example.com/image2.jpg");
        recipe2.setMealTime("Lunch");
        recipe2.setTotalCalories(500);
        recipe2.setTotalProtein(30);
        recipe2.setTotalFat(20);
        recipe2.setTotalCarbohydrates(60);
        recipe2.setWeek("2024-W23");

        List<Recipe> expectedRecipes = Arrays.asList(recipe1, recipe2);

        // Mock dBRepository's getAllRecipes metode
        when(dBRepository.getAllRecipes()).thenReturn(expectedRecipes);

        // Kald metoden og få resultatet
        List<Recipe> actualRecipes = useCase.getAllRecipes();

        // Assert at de forventede og faktiske resultater er ens
        assertEquals(expectedRecipes, actualRecipes);
    }
}
//Det er ikke en fejl, der kommer når den køres, men en Warning om at loading of agents bliver disallowed i future release