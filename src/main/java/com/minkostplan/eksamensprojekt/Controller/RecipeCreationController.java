package com.minkostplan.eksamensprojekt.Controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class RecipeCreationController {

    @GetMapping("/recipe-creation")
    public String showRecipeForm() {
        return "recipe-creation";
    }

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @PostMapping("/recipe-creation")
    public String createRecipe(@RequestParam("title") String title,
                               @RequestParam("description") String description,
                               @RequestParam("ingredients") List<String> ingredients,
                               @RequestParam("instructions") String instructions,
                               @RequestParam("cookingTime") String cookingTime,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               Model model) {

        // Save the uploaded file
        String imageUrl = null;
        if (!imageFile.isEmpty()) {
            try {
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get(UPLOAD_DIR + imageFile.getOriginalFilename());
                Files.write(path, bytes);
                imageUrl = "/images/" + imageFile.getOriginalFilename(); // The URL to access the image
            } catch (IOException e) {
                e.printStackTrace();
                // Handle error
            }
        }

        Recipe recipe = new Recipe(title, description, ingredients, instructions, cookingTime, imageUrl);
        model.addAttribute("recipe", recipe);

        return "recipe-success";
    }
}
