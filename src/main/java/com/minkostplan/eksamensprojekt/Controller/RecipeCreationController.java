package com.minkostplan.eksamensprojekt.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Service.UseCase;

@Controller
public class RecipeCreationController {

    @Autowired
    private UseCase useCase;

    @GetMapping("/recipe-creation")
    public String showCreateRecipeForm(Model model) {
        List<Ingredient> ingredientList = useCase.getAllIngredients();
        model.addAttribute("ingredientsList", ingredientList);
        return "recipe-creation";
    }

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

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
                                          @RequestParam("imageFile") MultipartFile imageFile,
                                          Model model) {

        // Save the uploaded file
        String imageUrl = null;
        if (!imageFile.isEmpty()) {
            try {
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get(UPLOAD_DIR + imageFile.getOriginalFilename());
                Files.write(path, bytes);
                imageUrl = "/images/" + imageFile.getOriginalFilename();
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Fejl ved upload af billede");
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

        // Calculate total nutritional values
        int totalCalories = 0;
        int totalProtein = 0;
        int totalFat = 0;
        int totalCarbohydrates = 0;

        for (int i = 0; i < ingredientIds.size(); i++) {
            Ingredient ingredient = useCase.getIngredientById(ingredientIds.get(i));
            double quantity = quantities.get(i);
            totalCalories += ingredient.getCalories() * quantity / 100;
            totalProtein += ingredient.getProtein() * quantity / 100;
            totalFat += ingredient.getFat() * quantity / 100;
            totalCarbohydrates += ingredient.getCarbohydrate() * quantity / 100;
        }

        recipe.setTotalCalories(totalCalories);
        recipe.setTotalProtein(totalProtein);
        recipe.setTotalFat(totalFat);
        recipe.setTotalCarbohydrates(totalCarbohydrates);

        useCase.createRecipeWithIngredients(recipe, ingredientIds, quantities);

        // Return success message as JSON response
        return ResponseEntity.ok().body("{\"message\": \"Opskriften blev oprettet!\"}");
    }
}
