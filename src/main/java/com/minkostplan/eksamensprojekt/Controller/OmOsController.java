package com.minkostplan.eksamensprojekt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OmOsController {

    @GetMapping("/omOs")
    public String showOmOsPage() {
        return "omOs"; // Assuming "omOs" is the name of your Thymeleaf template
    }
}
