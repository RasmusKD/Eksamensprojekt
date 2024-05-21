package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.HashMap;

@Controller
public class DashboardController {

    @Autowired
    private UseCase useCase;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName(); // Get the logged-in user's email
        User user = useCase.getUserByEmail(email); // Retrieve user info from the database

        if (user.getEmployed() == 0) {
            return "redirect:/customer-dashboard.html";
        } else if (user.getEmployed() == 1) {
            return "redirect:/employee-dashboard.html";
        } else if (user.getEmployed() == 2) {
            return "redirect:/admin-dashboard.html";
        }

        // Default to customer dashboard if no matching role found
        return "redirect:/customer-dashboard.html";
    }

    @PostMapping("/update-goal")
    public String updateGoal(@RequestParam("goal") int goal, Authentication authentication) {
        String email = authentication.getName(); // Get the logged-in user's email
        User user = useCase.getUserByEmail(email); // Retrieve user info from the database
        user.setGoal(goal); // Update the user's goal
        useCase.updateUser(user); // Save the updated user to the database

        return "redirect:/dashboard"; // Redirect back to the dashboard
    }

    @PostMapping("/update-user")
    public String updateUser(
            @RequestParam("age") int age,
            @RequestParam("gender") char gender,
            @RequestParam("weight") double weight,
            @RequestParam("height") double height,
            @RequestParam("activityLevel") int activityLevel,
            @RequestParam("goal") int goal,
            Authentication authentication) {

        String email = authentication.getName(); // Get the logged-in user's email
        User user = useCase.getUserByEmail(email); // Retrieve user info from the database

        // Update user details
        user.setAge(age);
        user.setGender(gender);
        user.setWeight(weight);
        user.setHeight(height);
        user.setActivityLevel(activityLevel);
        user.setGoal(goal);

        // Save the updated user to the database
        useCase.updateUser(user);

        return "redirect:/dashboard"; // Redirect back to the dashboard
    }
}
