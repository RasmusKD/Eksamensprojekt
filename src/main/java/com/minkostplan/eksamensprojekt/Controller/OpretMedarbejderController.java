package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                                         RedirectAttributes redirectAttributes) {
        try {
            User user = useCase.getUserByEmail(email);
            if (user != null) {
                useCase.updateEmploymentStatus(email, employmentStatus);
                redirectAttributes.addFlashAttribute("message", "Employment status updated successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "User not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred while updating the employment status.");
        }
        return "redirect:/opret-medarbejder";
    }
}
