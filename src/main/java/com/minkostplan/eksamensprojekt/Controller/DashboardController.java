package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Subscription;
import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private UseCase useCase;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName(); // Get the logged-in user's email
        User user = useCase.getUserByEmail(email); // Retrieve user info from the database

        // Mapping activity levels and goals
        Map<Integer, String> activityLevelMap = new HashMap<>();
        activityLevelMap.put(0, "Ingen eller meget lidt aktiv");
        activityLevelMap.put(1, "1-2 gange om ugen");
        activityLevelMap.put(2, "3-5 gange om ugen");
        activityLevelMap.put(3, "6-7 gange om ugen");
        activityLevelMap.put(4, "To gange om dagen");

        Map<Integer, String> goalMap = new HashMap<>();
        goalMap.put(0, "Vægttab");
        goalMap.put(1, "Vægtøgning");
        goalMap.put(2, "Muskelopbygning");
        goalMap.put(3, "Vedligehold vægt");

        // Calculate calories
        double calorieNeeds = useCase.calculateCalories(user);

        // Retrieve the subscription (active or not)
        Subscription subscription = useCase.getSubscriptionByUserId(user.getUserId());
        long daysLeft = 0;
        if (subscription != null) {
            LocalDate endDate = convertToLocalDate(subscription.getEndDate());
            LocalDate currentDate = LocalDate.now();
            daysLeft = ChronoUnit.DAYS.between(currentDate, endDate);
            if (daysLeft < 0) {
                daysLeft = 0;
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("activityLevelMap", activityLevelMap);
        model.addAttribute("goalMap", goalMap);
        model.addAttribute("calorieNeeds", calorieNeeds); // Add calorie needs to the model
        model.addAttribute("userId", user.getUserId()); // Add user ID to the model
        model.addAttribute("daysLeft", daysLeft); // Add days left to the model
        model.addAttribute("subscription", subscription); // Add subscription to the model

        return "dashboard";
    }

    private LocalDate convertToLocalDate(java.util.Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }
}
