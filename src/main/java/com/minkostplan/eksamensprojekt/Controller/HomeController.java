package com.minkostplan.eksamensprojekt.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller-klasse til håndtering af hjem- og login-siderne.
 */
@Controller
public class HomeController {

    /**
     * Viser landingssiden (home) eller omdirigerer til dashboardet, hvis brugeren er logget ind.
     *
     * @return Navnet på viewet "home" eller redirect til "dashboard".
     */
    @GetMapping("/")
    public String landing() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return "redirect:/weekly-recipes";
        }
        return "home";
    }

    /**
     * Viser login-siden eller omdirigerer til dashboardet, hvis brugeren er logget ind.
     *
     * @return Navnet på viewet "login" eller redirect til "dashboard".
     */
    @GetMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return "redirect:/weekly-recipes";
        }
        return "login";
    }
}
