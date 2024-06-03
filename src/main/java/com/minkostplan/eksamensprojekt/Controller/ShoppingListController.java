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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopping-list")
public class ShoppingListController {

    @Autowired
    private UseCase useCase;

    @GetMapping
    public String getShoppingList(Model model, Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        List<Ingredient> shoppingList = useCase.getShoppingList(user.getUserId());
        model.addAttribute("shoppingList", shoppingList);
        model.addAttribute("page", "shopping-list");
        return "shoppingList";
    }

    @PostMapping("/add-to-shopping-list/{recipeId}")
    @ResponseBody
    public String addToShoppingList(@PathVariable int recipeId, Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        Recipe recipe = useCase.getRecipeById(recipeId);
        if (recipe == null) {
            return "Recipe not found";
        }

        double userCaloricNeeds = useCase.calculateCalories(user);
        double adjustedCalories = useCase.calculateAdjustedCalories(userCaloricNeeds, recipe.getMealTime());
        List<Ingredient> adjustedIngredients = useCase.getAdjustedIngredients(recipe, adjustedCalories);

        useCase.addIngredientsToShoppingList(user, adjustedIngredients);

        return "Ingredienser tilføjet til indkøbslisten!";
    }

    @PostMapping("/update-bought-status")
    @ResponseBody
    public String updateBoughtStatus(@RequestBody Map<String, Object> payload, Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        int ingredientId = (int) payload.get("ingredientId");
        boolean bought = (boolean) payload.get("bought");
        useCase.updateBoughtStatus(user.getUserId(), ingredientId, bought);
        return "Status opdateret!";
    }

    @PostMapping("/clear-all")
    @ResponseBody
    public String clearAll(Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        useCase.clearAll(user.getUserId());
        return "All ingredients cleared";
    }

    @PostMapping("/clear-marked")
    @ResponseBody
    public String clearMarked(Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        useCase.clearMarked(user.getUserId());
        return "Marked ingredients cleared";
    }
}
