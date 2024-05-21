package com.minkostplan.eksamensprojekt.Controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.minkostplan.eksamensprojekt.Model.Recipe;

@Controller
public class RecipeCreationController {

    @GetMapping("/recipe-creation")
    public String showRecipeForm() {
        return "recipe-creation";
    }

    @PostMapping("/recipe-creation")
    public String createRecipe(@RequestParam("title") String title,
                               @RequestParam("description") String description,
                               @RequestParam("ingredients") List<String> ingredients,
                               @RequestParam("method") String method,
                               @RequestParam("cookingTime") String cookingTime,
                               @RequestParam("imageUrl") String imageUrl,
                               Model model) {

        Recipe recipe = new Recipe(title, description, ingredients, method, cookingTime, imageUrl);
        System.out.println("test");
        model.addAttribute("recipe", recipe);

        return "recipe-success";
    }
}