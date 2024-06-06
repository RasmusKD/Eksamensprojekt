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

/**
 * Controller klasse, der håndterer indkøbsliste-relaterede anmodninger.
 */
@Controller
@RequestMapping("/shopping-list")
public class ShoppingListController {

    @Autowired
    private UseCase useCase;

    /**
     * Henter indkøbslisten for den aktuelle bruger.
     *
     * @param model      Model objekt til at tilføje attributter.
     * @param principal  Brugerens sikkerhedsdetaljer.
     * @return Navn på visningssiden for indkøbslisten.
     */
    @GetMapping
    public String getShoppingList(Model model, Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        List<Ingredient> shoppingList = useCase.getShoppingList(user.getUserId());
        model.addAttribute("shoppingList", shoppingList);
        model.addAttribute("page", "shopping-list");
        return "shoppingList";
    }

    /**
     * Tilføjer ingredienser fra en opskrift til brugerens indkøbsliste.
     *
     * @param recipeId   ID på opskriften, der skal tilføjes.
     * @param principal  Brugerens sikkerhedsdetaljer.
     * @return En besked der indikerer succes eller fejl.
     */
    @PostMapping("/add-to-shopping-list/{recipeId}")
    @ResponseBody
    public String addToShoppingList(@PathVariable int recipeId, Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        Recipe recipe = useCase.getRecipeById(recipeId);
        if (recipe == null) {
            return "Opskrift ikke Fundet";
        }

        double userCaloricNeeds = useCase.calculateCalories(user);
        double adjustedCalories = useCase.calculateAdjustedCalories(userCaloricNeeds, recipe.getMealTime());
        List<Ingredient> adjustedIngredients = useCase.getAdjustedIngredients(recipe, adjustedCalories);

        useCase.addIngredientsToShoppingList(user, adjustedIngredients);

        return "Ingredienser tilføjet til indkøbslisten!";
    }

    /**
     * Opdaterer "købt" status for en ingrediens på indkøbslisten.
     *
     * @param payload    Data der indeholder ingredientId og købt(bought) status.
     * @param principal  Brugerens sikkerhedsdetaljer.
     * @return En besked der indikerer succes.
     */
    @PostMapping("/update-bought-status")
    @ResponseBody
    public String updateBoughtStatus(@RequestBody Map<String, Object> payload, Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        int ingredientId = (int) payload.get("ingredientId");
        boolean bought = (boolean) payload.get("bought");
        useCase.updateBoughtStatus(user.getUserId(), ingredientId, bought);
        return "Status opdateret!";
    }

    /**
     * Rydder alle ingredienser fra brugerens indkøbsliste.
     *
     * @param principal  Brugerens sikkerhedsdetaljer.
     * @return En besked der indikerer succes.
     */
    @PostMapping("/clear-all")
    @ResponseBody
    public String clearAll(Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        useCase.clearAll(user.getUserId());
        return "Alle ingredienser fjernet";
    }

    /**
     * Rydder alle markerede (købte) ingredienser fra brugerens indkøbsliste.
     *
     * @param principal  Brugerens sikkerhedsdetaljer.
     * @return En besked der indikerer succes.
     */
    @PostMapping("/clear-marked")
    @ResponseBody
    public String clearMarked(Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        useCase.clearMarked(user.getUserId());
        return "Markerede ingredienser fjernet";
    }

    /**
     * Opdaterer mængden af en ingrediens på indkøbslisten.
     *
     * @param payload    Data der indeholder ingredientId og den nye mængde.
     * @param principal  Brugerens sikkerhedsdetaljer.
     * @return En besked der indikerer succes.
     */
    @PostMapping("/update-quantity")
    @ResponseBody
    public String updateQuantity(@RequestBody Map<String, Object> payload, Principal principal) {
        User user = useCase.getUserByEmail(principal.getName());
        int ingredientId = (int) payload.get("ingredientId");
        int quantity = Integer.parseInt(payload.get("quantity").toString());
        useCase.updateQuantity(user.getUserId(), ingredientId, quantity);
        return "Antal opdateret!";
    }
}
