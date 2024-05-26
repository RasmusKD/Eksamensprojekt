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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class WeeklyRecipesController {

    private final UseCase useCase;
    private static final List<String> DAY_NAMES = Arrays.asList("Mandag", "Tirsdag", "Onsdag", "Torsdag", "Fredag", "Lørdag", "Søndag");

    @Autowired
    public WeeklyRecipesController(UseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/weekly-recipes")
    public String getWeeklyRecipes(Model model, @RequestParam(value = "day", required = false) Integer dayOffset) {
        if (dayOffset == null) {
            // Default to current day of the week
            dayOffset = LocalDate.now().getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
        }

        // Wrap-around logic for dayOffset
        if (dayOffset < 0) {
            dayOffset = 6;
        } else if (dayOffset > 6) {
            dayOffset = 0;
        }

        LocalDate currentDate = LocalDate.now().with(DayOfWeek.MONDAY).plusDays(dayOffset);

        LocalDate previousDay = currentDate.minusDays(1);
        LocalDate nextDay = currentDate.plusDays(1);

        List<Recipe> currentDayRecipes = getMealsWithPlaceholders(currentDate.format(DateTimeFormatter.ISO_DATE));
        List<Recipe> previousDayRecipes = getMealsWithPlaceholders(previousDay.format(DateTimeFormatter.ISO_DATE));
        List<Recipe> nextDayRecipes = getMealsWithPlaceholders(nextDay.format(DateTimeFormatter.ISO_DATE));

        model.addAttribute("currentDayName", DAY_NAMES.get(currentDate.getDayOfWeek().getValue() - 1));
        model.addAttribute("previousDayName", DAY_NAMES.get(previousDay.getDayOfWeek().getValue() - 1));
        model.addAttribute("nextDayName", DAY_NAMES.get(nextDay.getDayOfWeek().getValue() - 1));

        model.addAttribute("currentDay", currentDate);
        model.addAttribute("previousDay", previousDay);
        model.addAttribute("nextDay", nextDay);

        model.addAttribute("currentDayRecipes", currentDayRecipes);
        model.addAttribute("previousDayRecipes", previousDayRecipes);
        model.addAttribute("nextDayRecipes", nextDayRecipes);

        model.addAttribute("previousDayOffset", dayOffset - 1);
        model.addAttribute("nextDayOffset", dayOffset + 1);

        model.addAttribute("dayOffset", dayOffset);

        return "weekly-recipes";
    }

    private List<Recipe> getMealsWithPlaceholders(String date) {
        List<Recipe> recipes = useCase.getRecipesByDay(date);
        List<Recipe> mealsWithPlaceholders = new ArrayList<>(Arrays.asList(new Recipe[3]));
        for (Recipe recipe : recipes) {
            switch (recipe.getMealTime()) {
                case "Breakfast":
                    mealsWithPlaceholders.set(0, recipe);
                    break;
                case "Lunch":
                    mealsWithPlaceholders.set(1, recipe);
                    break;
                case "Dinner":
                    mealsWithPlaceholders.set(2, recipe);
                    break;
            }
        }
        return mealsWithPlaceholders;
    }
}
