package com.minkostplan.eksamensprojekt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller-klasse til håndtering af "Om os"-siden.
 */
@Controller
public class OmOsController {

    /**
     * Viser "Om os"-siden.
     *
     * @return Navnet på viewet "omOs".
     */
    @GetMapping("/omOs")
    public String showOmOsPage(Model model) {
        model.addAttribute("page", "omOs");
        return "omOs";
    }
}