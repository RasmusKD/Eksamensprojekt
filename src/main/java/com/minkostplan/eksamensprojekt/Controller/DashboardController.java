package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

        model.addAttribute("user", user);
        model.addAttribute("activityLevelMap", activityLevelMap);
        model.addAttribute("goalMap", goalMap);

        return "dashboard";
    }


}
