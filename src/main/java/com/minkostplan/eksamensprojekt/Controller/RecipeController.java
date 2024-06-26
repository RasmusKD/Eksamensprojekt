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

        // Calculate macronutrient percentages
        double fatCalories = recipe.getTotalFat() * 9;
        double proteinCalories = recipe.getTotalProtein() * 4;
        double carbCalories = recipe.getTotalCarbohydrates() * 4;

        double totalMacroCalories = fatCalories + proteinCalories + carbCalories;
        double fatPercentage = (fatCalories / totalMacroCalories) * 100;
        double proteinPercentage = (proteinCalories / totalMacroCalories) * 100;
        double carbPercentage = (carbCalories / totalMacroCalories) * 100;

        // Calculate stroke-dasharray and stroke-dashoffset values
        String fatDashArray = fatPercentage + ", " + (100 - fatPercentage);
        String proteinDashArray = proteinPercentage + ", " + (100 - proteinPercentage);
        String carbDashArray = carbPercentage + ", " + (100 - carbPercentage);

        double proteinDashOffset = 25 - fatPercentage;
        double carbDashOffset = 25 - fatPercentage - proteinPercentage;

        model.addAttribute("recipe", recipe);
        model.addAttribute("fatPercentage", fatPercentage);
        model.addAttribute("proteinPercentage", proteinPercentage);
        model.addAttribute("carbPercentage", carbPercentage);

        model.addAttribute("fatDashArray", fatDashArray);
        model.addAttribute("proteinDashArray", proteinDashArray);
        model.addAttribute("carbDashArray", carbDashArray);

        model.addAttribute("proteinDashOffset", proteinDashOffset);
        model.addAttribute("carbDashOffset", carbDashOffset);

        List<String> methodSentences = Arrays.stream(recipe.getMethod().split("\\. "))
                .map(sentence -> sentence.trim() + ".")
                .collect(Collectors.toList());

        model.addAttribute("methodSentences", methodSentences);
        return "recipe";
    }



    /**
     * Tilføjer en opskrift til brugerens favoritter.
     *
     * @param recipeId  Opskriftens ID.
     * @param principal Principal-objekt, der indeholder oplysninger om den loggede bruger.
     * @return En besked om, at opskriften blev tilføjet til favoritter.
     */
    @PostMapping("/favorite-recipe/{recipeId}")
    @ResponseBody
    public String favoriteRecipe(@PathVariable int recipeId, Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        useCase.addFavoriteRecipe(user.getUserId(), recipeId);
        return "Recipe favorited successfully.";
    }
}
