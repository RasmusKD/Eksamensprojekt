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

    /**
     * Håndterer GET-forespørgsler til /weekly-recipes og returnerer ugens opskrifter organiseret efter måltidstid.
     *
     * @param model         Model-objektet der bruges til at tilføje attributter til visningen.
     * @param userDetails   Detaljer om den autentificerede bruger.
     * @param week          (valgfri) Uge i formatet 'yyyy-Www'. Hvis ikke angivet, bruges den aktuelle uge.
     * @return Navnet på den HTML-side der skal vises.
     */
    @GetMapping("/weekly-recipes")
    public String getWeeklyRecipes(Model model, @AuthenticationPrincipal UserDetails userDetails, @RequestParam(value = "week", required = false) String week) {
        if (week == null) {
            week = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-'W'ww"));
        }

        List<Recipe> recipes = useCase.getRecipesByWeek(week);
        List<List<Recipe>> organizedRecipes = organizeRecipesByMealTime(recipes);

        boolean isSubscriber = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SUBSCRIBER"));

        User user = useCase.getUserByEmail(userDetails.getUsername());
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

    /**
     * Organiserer en liste af opskrifter efter måltidstid.
     *
     * @param recipes Liste af opskrifter der skal organiseres.
     * @return En liste med tre underlister: en for morgenmad, en for frokost og en for aftensmad.
     */
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

    /**
     * Håndterer POST-forespørgsler til /toggle-favorite og skifter favoritstatus for en opskrift.
     *
     * @param payload En mappe der indeholder brugerens ID og opskriftens ID.
     * @return En mappe der indikerer succes og den nye favoritstatus.
     */
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

    /**
     * Håndterer GET-forespørgsler til /favorite-recipes og returnerer brugerens favoritopskrifter organiseret efter måltidstid.
     *
     * @param model       Model-objektet der bruges til at tilføje attributter til visningen.
     * @param userDetails Detaljer om den autentificerede bruger.
     * @return Navnet på den HTML-side der skal vises.
     */
    @GetMapping("/favorite-recipes")
    public String getFavoriteRecipes(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = useCase.getUserByEmail(userDetails.getUsername());

        List<Integer> favoriteRecipeIds = useCase.getFavoriteRecipeIdsByUserId(user.getUserId());
        List<Recipe> favoriteRecipes = new ArrayList<>();
        for (Integer recipeId : favoriteRecipeIds) {
            Recipe recipe = useCase.getRecipeById(recipeId);
            if (recipe != null) {
                favoriteRecipes.add(recipe);
            }
        }

        List<List<Recipe>> organizedRecipes = organizeRecipesByMealTime(favoriteRecipes);

        model.addAttribute("breakfastRecipes", organizedRecipes.get(0));
        model.addAttribute("lunchRecipes", organizedRecipes.get(1));
        model.addAttribute("dinnerRecipes", organizedRecipes.get(2));
        model.addAttribute("page", "favorite-recipes");
        model.addAttribute("user", user);

        return "favorite-recipes";
    }
}
