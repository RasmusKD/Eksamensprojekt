package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller-klasse til håndtering af forespørgsler relateret til opskrifter.
 */
@Controller
public class RecipeController {

    @Autowired
    private UseCase useCase;

    /**
     * Henter opskrifter for en bestemt dag.
     *
     * @param day       Dagen for hvilke opskrifter skal hentes.
     * @param model     Model-objekt til at tilføje attributter.
     * @param principal Principal-objekt, der indeholder oplysninger om den loggede bruger.
     * @return Navnet på viewet "recipesByDay".
     */
    @GetMapping("/recipes/{day}")
    public String getRecipesByDay(@PathVariable String day, Model model, Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        List<Recipe> recipes = useCase.getRecipesByWeekWithAdjustedCalories(day, user);
        model.addAttribute("recipes", recipes);
        return "recipesByDay";
    }

    /**
     * Henter en opskrift ved dens ID.
     *
     * @param id        Opskriftens ID.
     * @param model     Model-objekt til at tilføje attributter.
     * @param principal Principal-objekt, der indeholder oplysninger om den loggede bruger.
     * @return Navnet på viewet "recipe".
     */
    @GetMapping("/recipe/{id}")
    public String getRecipeById(@PathVariable int id, Model model, Principal principal) {
        Recipe recipe = useCase.getRecipeById(id);
        User user = useCase.getUserByEmail(principal.getName());
        double userCaloricNeeds = useCase.calculateCalories(user);
        double adjustedCalories = useCase.calculateAdjustedCalories(userCaloricNeeds, recipe.getMealTime());
        recipe.setAdjustedCalories((int) adjustedCalories);
        List<Ingredient> adjustedIngredients = useCase.getAdjustedIngredients(recipe, adjustedCalories);
        recipe.setIngredients(adjustedIngredients);

        model.addAttribute("recipe", recipe);

        List<String> methodSentences = Arrays.stream(recipe.getMethod().split("\\. "))
                .map(sentence -> sentence.trim() + ".")
                .collect(Collectors.toList());

        model.addAttribute("recipe", recipe);
        model.addAttribute("methodSentences", methodSentences);
        return "recipe";
    }

    @GetMapping("/shopping-list")
    public String getShoppingList(Model model, Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        List<Ingredient> shoppingList = useCase.getShoppingList(user.getUserId());
        model.addAttribute("shoppingList", shoppingList);
        return "shoppingList";
    }

    @PostMapping("/add-to-shopping-list/{recipeId}")
    @ResponseBody
    public String addToShoppingList(@PathVariable int recipeId, Principal principal) {
        Recipe recipe = useCase.getRecipeById(recipeId);
        if (recipe == null) {
            return "Recipe not found";
        }

        User user = useCase.getUserByEmail(principal.getName());
        List<Ingredient> ingredients = recipe.getIngredients();
        useCase.addIngredientsToShoppingList(user, ingredients);

        return "Ingredienser tilføjet til indkøbslisten!";
    }


    @PostMapping("/favorite-recipe/{recipeId}")
    @ResponseBody
    public String favoriteRecipe(@PathVariable int recipeId, Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        useCase.addFavoriteRecipe(user.getUserId(), recipeId);
        return "Recipe favorited successfully.";
    }
}


