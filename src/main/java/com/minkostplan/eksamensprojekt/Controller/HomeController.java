package com.minkostplan.eksamensprojekt.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String landing() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return "redirect:/dashboard";
        }
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return "redirect:/dashboard";
        }
        return "login";
    }
}
