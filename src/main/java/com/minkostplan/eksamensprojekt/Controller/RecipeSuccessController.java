package com.minkostplan.eksamensprojekt.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RecipeSuccessController {

    @GetMapping("/recipe-success")
    public String showSuccessPage() {
        return "recipe-success";
    }
}



//Denne side skal redirect til der, hvor man ser recipes i systemet.