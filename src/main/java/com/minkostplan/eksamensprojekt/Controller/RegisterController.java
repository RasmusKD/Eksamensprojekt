package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller-klasse til håndtering af brugerregistrering.
 */
@Controller
public class RegisterController {

    @Autowired
    private UseCase useCase;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Viser registreringsformularen.
     *
     * @param model Model-objekt til at tilføje attributter.
     * @return Navnet på viewet "register".
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return "redirect:/weekly-recipes";
        }
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Håndterer brugerregistrering.
     *
     * @param user Brugerobjektet, der skal registreres.
     * @return Redirect til login-siden.
     */
    @PostMapping("/register")
    public String handleRegistration(@ModelAttribute User user, Model model) {
        User existingUser = useCase.getUserByEmail(user.getEmail());
        if (existingUser != null) {
            model.addAttribute("message", "E-mailen er allerede registreret.");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        useCase.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/check-email")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        User existingUser = useCase.getUserByEmail(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", existingUser != null);
        return ResponseEntity.ok(response);
    }
}
