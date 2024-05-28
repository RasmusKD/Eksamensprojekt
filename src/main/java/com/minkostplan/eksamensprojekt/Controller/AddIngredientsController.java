package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//Controller-klasse til håndtering af forespørgsler om tilføjelse af ingredienser
@Controller
public class AddIngredientsController {

    //instruerer Spring til at injicere en instans af UseCase
    //hvilket gør det muligt at bruge UseCase-metoder i denne controller
    @Autowired
    private UseCase useCase; //private UseCase useCase: Deklarerer en privat variabel useCase af typen UseCase
                             // som vil blive brugt til at kalde forretningslogik-metoder.

    //Denne annotation specificerer, at metoden skal håndtere GET-forespørgsler til URL'en "/add-ingredients".
    @GetMapping("/add-ingredients")
    public String showAddIngredientsForm() {
        return "add-ingredients";
    }

    //Denne annotation specificerer, at metoden skal håndtere POST-forespørgsler til URL'en "/add-ingredients".
    @PostMapping("/add-ingredients")
    public String addIngredient(@RequestParam("name") String name,               //Metoden tager flere parametre som markeret med @RequestParam, hentes fra POST
                                @RequestParam("fat") double fat,
                                @RequestParam("carbohydrate") double carbohydrate,
                                @RequestParam("protein") double protein,
                                @RequestParam("calories") int calories) {
        Ingredient ingredient = new Ingredient(); //Opretter en ny instans af klassen Ingredient
        ingredient.setName(name);      //Bliver sat med værdien fra POST-forespørgslen
        ingredient.setFat(fat);         //Bliver sat med værdien fra POST-forespørgslen
        ingredient.setCarbohydrate(carbohydrate);   //Bliver sat med værdien fra POST-forespørgslen
        ingredient.setProtein(protein);         //osv
        ingredient.setCalories(calories);       //osv
        useCase.createIngredients(ingredient);  //osv
        return "redirect:/add-ingredients";     //Returnerer en omdirigering til URL'en "/add-ingredients"
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
