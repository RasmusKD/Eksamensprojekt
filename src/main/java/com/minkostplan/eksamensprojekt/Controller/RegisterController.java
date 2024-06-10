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


    //Spring finder useCase-objektet, som er en instans af UseCase-klassen.
    //useCase-objektet er annoteret med @Autowired, hvilket betyder, at Spring automatisk injicerer en instans af UseCase-klassen i controlleren.
    @Autowired
    private UseCase useCase;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Viser registreringsformularen.
     *
     * @param model Model-objekt til at tilføje attributter.
     * @return Navnet på viewet "register" eller en redirect til "weekly-recipes" hvis brugeren allerede er autentificeret.
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
     * @param user  Brugerobjektet, der skal registreres.
     * @param model Model-objekt til at tilføje attributter.
     * @return Navnet på viewet "register" med en besked, hvis e-mailen allerede er registreret, ellers en redirect til login-siden.
     */


    @PostMapping("/register") //håndtere post anmodning til endpointet register @Modelattribute User user fortæller spring at den skal binde brugerens formular til user objekt
    public String handleRegistration(@ModelAttribute User user, Model model) { //Model model er en grænseflade i spring der bruges til at sende data fra controller til visning

     //Opretter en variabel existingUser af typen User, og tildeler den værdien fra useCase.getUserByEmail(user.getEmail())
        User existingUser = useCase.getUserByEmail(user.getEmail()); //parameteren er user.getEmail, henter email fra det User objekt vi oprettede
        if (existingUser != null) { //if statement der tjekker om det resultat man får tilbage er null
            model.addAttribute("message", "E-mailen er allerede registreret.");//
            return "register"; //brugeren bliver sendt tilbage til registersiden
        }

        String newPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(newPassword);

        useCase.createUser(user); //kalder useCase metoden createUser med user objekt
        return "redirect:/login";
    }

    /**
     * Kontrollerer om en e-mail allerede er registreret.
     *
     * @param email E-mailen der skal kontrolleres.
     * @return En ResponseEntity med et map, der indikerer om e-mailen allerede er registreret.
     */
    @GetMapping("/check-email")
    @ResponseBody
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = useCase.getUserByEmail(email) != null;
        return ResponseEntity.ok(exists);
    }
}
