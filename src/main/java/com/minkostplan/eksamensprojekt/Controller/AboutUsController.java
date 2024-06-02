package com.minkostplan.eksamensprojekt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller-klasse til håndtering af "About Us"-siden.
 */
@Controller
public class AboutUsController {

    /**
     * Viser "About Us"-siden.
     *
     * @return Navnet på viewet "about-us".
     */
    @GetMapping("/about-us")
    public String showAboutUsPage(Model model) {
        model.addAttribute("page", "about-us");
        return "about-us";
    }
}