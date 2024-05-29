package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller-klasse til håndtering af forespørgsler om tilføjelse af ingredienser.
 */
@Controller
public class AddIngredientsController {

    // Instruerer Spring til at injicere en instans af UseCase
    // hvilket gør det muligt at bruge UseCase-metoder i denne controller
    @Autowired
    private UseCase useCase;

    /**
     * Viser formularen til tilføjelse af ingredienser.
     *
     * @param model Model-objekt til at tilføje attributter.
     * @return Navnet på viewet "add-ingredients".
     */
    @GetMapping("/add-ingredients")
    public String showAddIngredientsForm(Model model) {
        return "add-ingredients";
    }

    /**
     * Håndterer indsendelse af formular til tilføjelse af en ny ingrediens.
     *
     * @param name          Navnet på ingrediensen.
     * @param fat           Mængden af fedt i ingrediensen.
     * @param carbohydrate  Mængden af kulhydrater i ingrediensen.
     * @param protein       Mængden af protein i ingrediensen.
     * @param calories      Antallet af kalorier i ingrediensen.
     * @param model         Model-objekt til at tilføje attributter.
     * @return Navnet på viewet "add-ingredients".
     */
    @PostMapping("/add-ingredients")
    public String addIngredient(@RequestParam("name") String name,
                                @RequestParam("fat") double fat,
                                @RequestParam("carbohydrate") double carbohydrate,
                                @RequestParam("protein") double protein,
                                @RequestParam("calories") int calories,
                                Model model) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setFat(fat);
        ingredient.setCarbohydrate(carbohydrate);
        ingredient.setProtein(protein);
        ingredient.setCalories(calories);
        useCase.createIngredients(ingredient);

        model.addAttribute("message", "Ingrediens tilføjet!");

        return "add-ingredients";
    }

    /**
     * Viser formularen til redigering af ingredienser.
     *
     * @param model Model-objekt til at tilføje attributter.
     * @return Navnet på viewet "edit-ingredients".
     */
    @GetMapping("/edit-ingredients")
    public String showEditIngredientsForm(Model model) {
        model.addAttribute("ingredients", useCase.getAllIngredients());
        return "edit-ingredients";
    }

    /**
     * Henter detaljer om en specifik ingrediens.
     *
     * @param id Ingrediensens ID.
     * @return En Ingredient-objekt med detaljerne om ingrediensen.
     */
    @GetMapping("/ingredient-details/{id}")
    @ResponseBody
    public Ingredient getIngredientDetails(@PathVariable("id") int id) {
        return useCase.getIngredientById(id);
    }

    /**
     * Håndterer opdatering af en ingrediens.
     *
     * @param id            Ingrediensens ID.
     * @param name          Navnet på ingrediensen.
     * @param fat           Mængden af fedt i ingrediensen.
     * @param carbohydrate  Mængden af kulhydrater i ingrediensen.
     * @param protein       Mængden af protein i ingrediensen.
     * @param calories      Antallet af kalorier i ingrediensen.
     * @return Et map med en succesmeddelelse.
     */
    @PostMapping("/edit-ingredients")
    @ResponseBody
    public Map<String, String> editIngredient(@RequestParam("ingredientId") int id,
                                              @RequestParam("name") String name,
                                              @RequestParam("fat") double fat,
                                              @RequestParam("carbohydrate") double carbohydrate,
                                              @RequestParam("protein") double protein,
                                              @RequestParam("calories") int calories) {
        Ingredient ingredient = useCase.getIngredientById(id);
        ingredient.setName(name);
        ingredient.setFat(fat);
        ingredient.setCarbohydrate(carbohydrate);
        ingredient.setProtein(protein);
        ingredient.setCalories(calories);
        useCase.updateIngredient(ingredient);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Ingrediensen blev opdateret!");
        return response;
    }

    /**
     * Håndterer sletning af en ingrediens.
     *
     * @param id Ingrediensens ID.
     * @return Et map med en succes- eller fejmeddelelse.
     */
    @DeleteMapping("/delete-ingredient/{id}")
    @ResponseBody
    public Map<String, String> deleteIngredient(@PathVariable("id") int id) {
        boolean isDeleted = useCase.deleteIngredientById(id);
        Map<String, String> response = new HashMap<>();
        if (isDeleted) {
            response.put("message", "Ingredient deleted successfully");
        } else {
            response.put("message", "Failed to delete ingredient");
        }
        return response;
    }
}
