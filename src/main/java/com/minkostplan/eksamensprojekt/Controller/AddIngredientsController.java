package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller-klasse til håndtering af forespørgsler om tilføjelse af ingredienser.
 */
@Controller
public class AddIngredientsController {

    // Instruerer Spring til at injicere en instans af UseCase
    // hvilket gør det muligt at bruge UseCase-metoder i denne controller
    @Autowired  //instersere en instans af usecase
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
     * @return Navnet på viewet "add-ingredients".
     */
    @PostMapping("/add-ingredients")
    @ResponseBody
    public ResponseEntity<Map<String, String>> addIngredient(@RequestParam("name") String name,
                                                             @RequestParam("fat") double fat,
                                                             @RequestParam("carbohydrate") double carbohydrate,
                                                             @RequestParam("protein") double protein,
                                                             @RequestParam("calories") int calories) {
        // Opretter en ny ingrediens og sætter dens værdier
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setFat(fat);
        ingredient.setCarbohydrate(carbohydrate);
        ingredient.setProtein(protein);
        ingredient.setCalories(calories);
        // Tilføjer ingrediensen via useCase
        useCase.createIngredients(ingredient);

        // Forbereder JSON-svar med succesmeddelelse
        Map<String, String> response = new HashMap<>();
        response.put("message", "Ingrediens tilføjet!");
        return ResponseEntity.ok(response);
    }


    /**
     * Viser formularen til redigering af ingredienser.
     *
     * @param model Model-objekt til at tilføje attributter.
     * @return Navnet på viewet "edit-ingredients".
     */

    //Håndtere http Getanmodninger til URL'en edit-ingredients.
    @GetMapping("/edit-ingredients")
    public String showEditIngredientsForm(Model model) {//
        model.addAttribute("ingredients", useCase.getAllIngredients()); //Tilføjer en attribut til modellen med navnet
        return "edit-ingredients";                                  //"ingredients", som indeholder en liste af ingredienser hentet fra useCase.getAllIngredients().
    } //Returneres til siden edit-ingredients

    /**
     * Henter detaljer om en specifik ingrediens.
     *
     * @param id Ingrediensens ID.
     * @return Et Ingredient-objekt med detaljerne om ingrediensen.
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
     * @return JSON-respons med succesmeddelelse.
     */
    @PostMapping("/edit-ingredients")
    @ResponseBody
    public ResponseEntity<Map<String, String>> editIngredient(@RequestParam("ingredientId") int id,
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
        return ResponseEntity.ok(response);
    }


    /**
     * Håndterer sletning af en ingrediens.
     *
     * @param id Ingrediensens ID.
     * @return JSON-respons med succes- eller fejlmeddelelse.
     */
    @DeleteMapping("/delete-ingredient/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteIngredient(@PathVariable("id") int id) {
        boolean isDeleted = useCase.deleteIngredientById(id);
        Map<String, String> response = new HashMap<>();
        if (isDeleted) {
            response.put("message", "Ingrediensen blev slettet!");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Kunne ikke slette ingrediensen.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
