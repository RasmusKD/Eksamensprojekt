package com.minkostplan.eksamensprojekt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String landing() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
