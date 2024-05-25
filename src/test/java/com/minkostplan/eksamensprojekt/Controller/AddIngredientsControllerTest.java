package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import com.minkostplan.eksamensprojekt.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddIngredientsController.class)
public class AddIngredientsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    //@Import(SecurityConfig.class)  // Tilføj denne linje for at inkludere SecurityConfig
    private UseCase useCase;

    @Test
    public void testShowAddIngredientsForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/add-ingredients"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddIngredient() throws Exception {
        Ingredient ingredient = new Ingredient("Test Ingredient", 10.0, 20.0, 30.0, 100);

        mockMvc.perform(MockMvcRequestBuilders.post("/add-ingredients")
                        .param("name", ingredient.getName())
                        .param("fat", String.valueOf(ingredient.getFat()))
                        .param("carbohydrate", String.valueOf(ingredient.getCarbohydrate()))
                        .param("protein", String.valueOf(ingredient.getProtein()))
                        .param("calories", String.valueOf(ingredient.getCalories())))
                .andExpect(status().is3xxRedirection());

        verify(useCase, times(1)).createIngredients(any(Ingredient.class));
    }
}
