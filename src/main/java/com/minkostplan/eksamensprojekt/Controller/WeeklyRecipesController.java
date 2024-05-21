package com.minkostplan.eksamensprojekt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class WeeklyRecipesController {

    @GetMapping("/weekly-recipes")
    public String getWeeklyRecipes(Model model) {
        List<String> daysOfWeek = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        model.addAttribute("daysOfWeek", daysOfWeek);
        return "weekly-recipes";
    }
}
