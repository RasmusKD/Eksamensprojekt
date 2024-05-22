package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

@Controller
public class OpretMedarbejderController {

    @Autowired
    private UseCase useCase;

    @GetMapping("/opret-medarbejder")
    public String showCreateEmployeeForm() {
        return "opret-medarbejder"; // This should match the HTML file name without the extension
    }

    @PostMapping("/update-employment-status")
    public String updateEmploymentStatus(@RequestParam("email") String email,
                                         @RequestParam("employmentStatus") int employmentStatus,
                                         Model model) {
        try {
            User user = useCase.getUserByEmail(email);
            if (user != null) {
                useCase.updateEmploymentStatus(email, employmentStatus);
                model.addAttribute("message", "Ansættelsesstatus opdateret!");
            } else {
                model.addAttribute("error", "Bruger ikke fundet!");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Der opstod en fejl under opdatering af ansættelsesstatus.");
        }
        return "opret-medarbejder"; // Return the same view to stay on the page
    }
}
