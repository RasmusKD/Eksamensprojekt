package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
    public String getWeeklyRecipes(Model model, @RequestParam(value = "week", required = false) String week) {
        if (week == null) {
            week = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-'W'ww"));
        }

        List<Recipe> recipes = useCase.getRecipesByWeek(week);
        List<List<Recipe>> organizedRecipes = organizeRecipesByMealTime(recipes);

        model.addAttribute("week", week);
        model.addAttribute("breakfastRecipes", organizedRecipes.get(0));
        model.addAttribute("lunchRecipes", organizedRecipes.get(1));
        model.addAttribute("dinnerRecipes", organizedRecipes.get(2));

        return "weekly-recipes";
    }

    private List<List<Recipe>> organizeRecipesByMealTime(List<Recipe> recipes) {
        List<List<Recipe>> organizedRecipes = new ArrayList<>(Arrays.asList(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        for (Recipe recipe : recipes) {
            switch (recipe.getMealTime()) {
                case "Breakfast":
                    organizedRecipes.get(0).add(recipe);
                    break;
                case "Lunch":
                    organizedRecipes.get(1).add(recipe);
                    break;
                case "Dinner":
                    organizedRecipes.get(2).add(recipe);
                    break;
            }
        }
        return organizedRecipes;
    }
}

