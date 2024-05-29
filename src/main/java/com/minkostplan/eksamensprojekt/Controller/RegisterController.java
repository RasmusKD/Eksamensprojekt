package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
            return "redirect:/dashboard";
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
    public String handleRegistration(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        useCase.createUser(user);
        return "redirect:/login";
    }
}
