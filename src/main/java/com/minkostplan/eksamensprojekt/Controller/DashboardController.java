package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Subscription;
import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

/**
 * Controller-klasse til håndtering af forespørgsler relateret til brugerens dashboard.
 */
@Controller
public class DashboardController {

    @Autowired
    private UseCase useCase;

    /**
     * Viser dashboard-siden for den loggede bruger.
     *
     * @param authentication Autentificeringsobjekt, der indeholder oplysninger om den loggede bruger.
     * @param model          Model-objekt til at tilføje attributter.
     * @return Navnet på viewet "dashboard".
     */
    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String email = authentication.getName(); // Henter den loggede brugers email
        User user = useCase.getUserByEmail(email); // Henter brugeroplysninger fra databasen

        // Mapping af aktivitetsniveauer og mål
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

        // Beregner kaloriebehov
        double calorieNeeds = useCase.calculateCalories(user);

        // Henter abonnementsoplysninger (aktivt eller ej)
        Subscription subscription = useCase.getLatestSubscriptionByUserId(user.getUserId());
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
        model.addAttribute("calorieNeeds", calorieNeeds);
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("daysLeft", daysLeft);
        model.addAttribute("subscription", subscription);
        model.addAttribute("page", "dashboard");

        return "dashboard";
    }

    /**
     * Opdaterer brugerens mål.
     *
     * @param goal          Det nye mål.
     * @param authentication Autentificeringsobjekt, der indeholder oplysninger om den loggede bruger.
     * @return Redirect til dashboard-siden.
     */
    @PostMapping("/update-goal")
    public String updateGoal(@RequestParam("goal") int goal, Authentication authentication) {
        String email = authentication.getName();
        User user = useCase.getUserByEmail(email);
        user.setGoal(goal);
        useCase.updateUser(user);

        return "redirect:/dashboard";
    }

    /**
     * Opdaterer brugerens oplysninger.
     *
     * @param birthday       Brugerens fødselsdag.
     * @param gender         Brugerens køn.
     * @param weight         Brugerens vægt.
     * @param height         Brugerens højde.
     * @param activityLevel  Brugerens aktivitetsniveau.
     * @param goal           Brugerens mål.
     * @param authentication Autentificeringsobjekt, der indeholder oplysninger om den loggede bruger.
     * @return Redirect til dashboard-siden.
     */
    @PostMapping("/update-user")
    public String updateUser(
            @RequestParam("birthday") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthday,
            @RequestParam("gender") char gender,
            @RequestParam("weight") double weight,
            @RequestParam("height") double height,
            @RequestParam("activityLevel") int activityLevel,
            @RequestParam("goal") int goal,
            Authentication authentication) {

        String email = authentication.getName();
        User user = useCase.getUserByEmail(email);

        user.setBirthday(birthday);
        user.setGender(gender);
        user.setWeight(weight);
        user.setHeight(height);
        user.setActivityLevel(activityLevel);
        user.setGoal(goal);

        useCase.updateUser(user);

        return "redirect:/dashboard";
    }

    private LocalDate convertToLocalDate(java.util.Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }
}
