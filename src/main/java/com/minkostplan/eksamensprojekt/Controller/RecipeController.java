package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class RecipeController {

    @Autowired
    private UseCase useCase;

    @GetMapping("/recipes/{day}")
    public String getRecipesByDay(@PathVariable String day, Model model, Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        List<Recipe> recipes = useCase.getRecipesByDayWithAdjustedCalories(day, user);
        model.addAttribute("recipes", recipes);
        return "recipesByDay";
    }

    @GetMapping("/recipe/{id}")
    public String getRecipeById(@PathVariable int id, Model model, Principal principal) {
        Recipe recipe = useCase.getRecipeById(id);
        User user = useCase.getUserByEmail(principal.getName());
        double userCaloricNeeds = useCase.calculateCalories(user);
        double adjustedCalories = useCase.calculateAdjustedCalories(userCaloricNeeds, recipe.getMealTime());
        recipe.setAdjustedCalories((int) adjustedCalories);
        List<Ingredient> adjustedIngredients = useCase.getAdjustedIngredients(recipe, adjustedCalories);
        recipe.setIngredients(adjustedIngredients); // Set adjusted ingredients
        model.addAttribute("recipe", recipe);
        return "recipe";
    }
}
