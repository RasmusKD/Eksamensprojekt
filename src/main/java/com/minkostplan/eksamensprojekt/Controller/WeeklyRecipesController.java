package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Controller-klasse til håndtering af forespørgsler relateret til ugentlige opskrifter.
 */
@Controller
public class WeeklyRecipesController {

    private final UseCase useCase;

    @Autowired
    public WeeklyRecipesController(UseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/weekly-recipes")
    public String getWeeklyRecipes(Model model, @AuthenticationPrincipal UserDetails userDetails, @RequestParam(value = "week", required = false) String week) {
        if (week == null) {
            week = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-'W'ww"));
        }

        List<Recipe> recipes = useCase.getRecipesByWeek(week);
        List<List<Recipe>> organizedRecipes = organizeRecipesByMealTime(recipes);

        boolean isSubscriber = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUBSCRIBER"));

        // Hent bruger objektet
        User user = useCase.getUserByEmail(userDetails.getUsername());

        // Check if recipes are favorites
        List<Integer> favoriteRecipeIds = useCase.getFavoriteRecipeIdsByUserId(user.getUserId());
        for (Recipe recipe : recipes) {
            recipe.setFavorite(favoriteRecipeIds.contains(recipe.getRecipeId()));
        }

        model.addAttribute("week", week);
        model.addAttribute("breakfastRecipes", organizedRecipes.get(0));
        model.addAttribute("lunchRecipes", organizedRecipes.get(1));
        model.addAttribute("dinnerRecipes", organizedRecipes.get(2));
        model.addAttribute("page", "weekly-recipes");
        model.addAttribute("isSubscriber", isSubscriber);
        model.addAttribute("user", user);

        return "weekly-recipes";
    }

    private List<List<Recipe>> organizeRecipesByMealTime(List<Recipe> recipes) {
        List<List<Recipe>> organizedRecipes = new ArrayList<>(Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        for (Recipe recipe : recipes) {
            switch (recipe.getMealTime()) {
                case "Breakfast" -> organizedRecipes.get(0).add(recipe);
                case "Lunch" -> organizedRecipes.get(1).add(recipe);
                case "Dinner" -> organizedRecipes.get(2).add(recipe);
            }
        }
        return organizedRecipes;
    }

    @PostMapping("/toggle-favorite")
    @ResponseBody
    public Map<String, Object> toggleFavorite(@RequestBody Map<String, Object> payload) {
        try {
            int userId = (Integer) payload.get("userId");
            int recipeId = (Integer) payload.get("recipeId");
            boolean isFavorite = useCase.toggleFavoriteStatus(userId, recipeId);
            return Map.of("success", true, "isFavorite", isFavorite);
        } catch (ClassCastException | NumberFormatException e) {
            e.printStackTrace();
            return Map.of("success", false, "error", e.getMessage());
        }
    }
}
