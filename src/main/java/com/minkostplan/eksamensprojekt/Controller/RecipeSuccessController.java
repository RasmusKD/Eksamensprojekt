package com.minkostplan.eksamensprojekt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller-klasse til håndtering af succes-siden for opskrifter.
 */
@Controller
public class RecipeSuccessController {

    /**
     * Viser succes-siden efter oprettelse af opskrift.
     *
     * @return Navnet på viewet "recipe-success".
     */
    @GetMapping("/recipe-success")
    public String showSuccessPage() {
        return "recipe-success";
    }
}
