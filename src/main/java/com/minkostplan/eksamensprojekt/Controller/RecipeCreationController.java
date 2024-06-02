package com.minkostplan.eksamensprojekt.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

/**
 * Controller-klasse til håndtering af opskriftsoprettelse og -redigering.
 */
@Controller
public class RecipeCreationController {

    @Autowired
    private UseCase useCase;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    /**
     * Viser formularen til oprettelse af opskrift.
     *
     * @param model Model-objekt til at tilføje attributter.
     * @return Navnet på viewet "recipe-creation".
     */
    @GetMapping("/recipe-creation")
    public String showCreateRecipeForm(Model model) {
        List<Ingredient> ingredientList = useCase.getAllIngredients();
        model.addAttribute("ingredientsList", ingredientList);
        return "recipe-creation";
    }

    /**
     * Viser formularen til redigering af opskrifter.
     *
     * @param model Model-objekt til at tilføje attributter.
     * @return Navnet på viewet "edit-recipe".
     */
    @GetMapping("/edit-recipe")
    public String showEditRecipeForm(Model model) {
        List<Recipe> recipes = useCase.getAllRecipes();
        List<Ingredient> ingredientList = useCase.getAllIngredients();
        model.addAttribute("recipes", recipes);
        model.addAttribute("ingredientsList", ingredientList);
        return "edit-recipe";
    }

    /**
     * Henter detaljer om en specifik opskrift.
     *
     * @param id Opskriftens ID.
     * @return Et Recipe-objekt med detaljerne om opskriften.
     */
    @GetMapping("/edit-recipe/{id}")
    @ResponseBody
    public Recipe getRecipeDetails(@PathVariable("id") int id) {
        return useCase.getRecipeById(id);
    }

    /**
     * Opretter en ny opskrift med ingredienser.
     *
     * @param title        Opskriftens titel.
     * @param description  Opskriftens beskrivelse.
     * @param ingredientIds Ingrediensernes ID'er.
     * @param quantities   Mængder af ingredienser.
     * @param instructions Opskriftens instruktioner.
     * @param cookingTime  Opskriftens tilberedningstid.
     * @param mealTime     Opskriftens måltidstid.
     * @param day          Dagen opskriften er tilknyttet.
     * @param imageFile    Billedfil af opskriften.
     * @return JSON-respons med succesmeddelelse.
     */
    @PostMapping("/recipe-creation")
    @ResponseBody
    public ResponseEntity<?> createRecipe(@RequestParam("title") String title,
                                          @RequestParam("description") String description,
                                          @RequestParam("ingredients") List<Integer> ingredientIds,
                                          @RequestParam("quantities") List<Double> quantities,
                                          @RequestParam("instructions") String instructions,
                                          @RequestParam("cookingTime") String cookingTime,
                                          @RequestParam("mealTime") String mealTime,
                                          @RequestParam("day") String day,
                                          @RequestParam("imageFile") MultipartFile imageFile) {

        // Gemmer den uploadede fil
        String imageUrl = null;
        if (!imageFile.isEmpty()) {
            try {
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get(UPLOAD_DIR + imageFile.getOriginalFilename());
                Files.write(path, bytes);
                imageUrl = "/images/" + imageFile.getOriginalFilename();
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("{\"error\": \"Fejl ved upload af billede.\"}");
            }
        }

        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setMethod(instructions);
        recipe.setCookingTime(cookingTime);
        recipe.setImageUrl(imageUrl);
        recipe.setMealTime(mealTime);
        recipe.setDay(day);

        // Beregn samlede næringsværdier
        int totalCalories = 0;
        int totalProtein = 0;
        int totalFat = 0;
        int totalCarbohydrates = 0;

        for (int i = 0; i < ingredientIds.size(); i++) {
            Ingredient ingredient = useCase.getIngredientById(ingredientIds.get(i));
            double quantity = quantities.get(i);
            totalCalories += (int) (ingredient.getCalories() * quantity / 100);
            totalProtein += (int) (ingredient.getProtein() * quantity / 100);
            totalFat += (int) (ingredient.getFat() * quantity / 100);
            totalCarbohydrates += (int) (ingredient.getCarbohydrate() * quantity / 100);
        }

        recipe.setTotalCalories(totalCalories);
        recipe.setTotalProtein(totalProtein);
        recipe.setTotalFat(totalFat);
        recipe.setTotalCarbohydrates(totalCarbohydrates);

        useCase.createRecipeWithIngredients(recipe, ingredientIds, quantities);

        return ResponseEntity.ok().body("{\"message\": \"Opskriften blev oprettet!\"}");
    }

    /**
     * Redigerer en eksisterende opskrift.
     *
     * @param id           Opskriftens ID.
     * @param title        Opskriftens titel.
     * @param description  Opskriftens beskrivelse.
     * @param ingredientIds Ingrediensernes ID'er.
     * @param quantities   Mængder af ingredienser.
     * @param instructions Opskriftens instruktioner.
     * @param cookingTime  Opskriftens tilberedningstid.
     * @param mealTime     Opskriftens måltidstid.
     * @param day          Dagen opskriften er tilknyttet.
     * @param imageFile    Valgfri billedfil af opskriften.
     * @return JSON-respons med succesmeddelelse.
     */
    @PostMapping("/edit-recipe")
    @ResponseBody
    public ResponseEntity<Map<String, String>> editRecipe(@RequestParam("recipeId") int id,
                                                          @RequestParam("title") String title,
                                                          @RequestParam("description") String description,
                                                          @RequestParam("ingredients") List<Integer> ingredientIds,
                                                          @RequestParam("quantities") List<Double> quantities,
                                                          @RequestParam("instructions") String instructions,
                                                          @RequestParam("cookingTime") String cookingTime,
                                                          @RequestParam("mealTime") String mealTime,
                                                          @RequestParam("day") String day,
                                                          @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        Recipe recipe = useCase.getRecipeById(id);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get(UPLOAD_DIR + imageFile.getOriginalFilename());
                Files.write(path, bytes);
                recipe.setImageUrl("/images/" + imageFile.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                Map<String, String> response = new HashMap<>();
                response.put("message", "Fejl ved upload af billede");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }

        recipe.setTitle(title);
        recipe.setDescription(description);
        recipe.setMethod(instructions);
        recipe.setCookingTime(cookingTime);
        recipe.setMealTime(mealTime);
        recipe.setDay(day);

        int totalCalories = 0;
        int totalProtein = 0;
        int totalFat = 0;
        int totalCarbohydrates = 0;

        for (int i = 0; i < ingredientIds.size(); i++) {
            Ingredient ingredient = useCase.getIngredientById(ingredientIds.get(i));
            double quantity = quantities.get(i);
            totalCalories += (int) (ingredient.getCalories() * quantity / 100);
            totalProtein += (int) (ingredient.getProtein() * quantity / 100);
            totalFat += (int) (ingredient.getFat() * quantity / 100);
            totalCarbohydrates += (int) (ingredient.getCarbohydrate() * quantity / 100);
        }

        recipe.setTotalCalories(totalCalories);
        recipe.setTotalProtein(totalProtein);
        recipe.setTotalFat(totalFat);
        recipe.setTotalCarbohydrates(totalCarbohydrates);

        useCase.updateRecipeWithIngredients(recipe, ingredientIds, quantities);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Opskriften blev opdateret!");
        return ResponseEntity.ok(response);
    }

    /**
     * Sletter en opskrift baseret på ID.
     *
     * @param id Opskriftens ID.
     * @return JSON-respons med succes- eller fejlmeddelelse.
     */
    @DeleteMapping("/delete-recipe/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteRecipe(@PathVariable("id") int id) {
        boolean isDeleted = useCase.deleteRecipeById(id);
        Map<String, String> response = new HashMap<>();
        if (isDeleted) {
            response.put("message", "Opskriften blev slettet!");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Kunne ikke slette opskriften.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
