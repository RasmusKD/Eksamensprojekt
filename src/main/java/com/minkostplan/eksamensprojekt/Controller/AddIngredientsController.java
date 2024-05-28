package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AddIngredientsController {

    @Autowired
    private UseCase useCase;

    @GetMapping("/add-ingredients")
    public String showAddIngredientsForm() {
        return "add-ingredients";
    }

    @PostMapping("/add-ingredients")
    public String addIngredient(@RequestParam("name") String name,
                                @RequestParam("fat") double fat,
                                @RequestParam("carbohydrate") double carbohydrate,
                                @RequestParam("protein") double protein,
                                @RequestParam("calories") int calories) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setFat(fat);
        ingredient.setCarbohydrate(carbohydrate);
        ingredient.setProtein(protein);
        ingredient.setCalories(calories);
        useCase.createIngredients(ingredient);
        return "redirect:/add-ingredients";
    }

    @GetMapping("/edit-ingredients")
    public String showEditIngredientsForm(Model model) {
        model.addAttribute("ingredients", useCase.getAllIngredients());
        return "edit-ingredients";
    }

    @GetMapping("/ingredient-details/{id}")
    @ResponseBody
    public Ingredient getIngredientDetails(@PathVariable("id") int id) {
        return useCase.getIngredientById(id);
    }

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
