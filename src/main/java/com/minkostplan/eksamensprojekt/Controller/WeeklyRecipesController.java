package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class WeeklyRecipesController {

    private final UseCase useCase;

    @Autowired
    public WeeklyRecipesController(UseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/weekly-recipes")
    public String getWeeklyRecipes(Model model) {
        List<Recipe> recipes = useCase.getAllRecipes(); // Assuming you have a method to get all recipes
        model.addAttribute("recipes", recipes);
        return "weekly-recipes";
    }

    @GetMapping("/recipe/{id}")
    public String getRecipeById(@PathVariable int id, Model model) {
        Recipe recipe = useCase.getRecipeById(id); // Assuming you have a method to get a recipe by ID
        model.addAttribute("recipe", recipe);
        return "recipe";
    }
}
