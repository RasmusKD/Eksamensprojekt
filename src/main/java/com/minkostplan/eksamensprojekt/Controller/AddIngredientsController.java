package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//Controller-klasse til håndtering af forespørgsler om tilføjelse af ingredienser
@Controller
public class AddIngredientsController {

    //instruerer Spring til at injicere en instans af UseCase
    //hvilket gør det muligt at bruge UseCase-metoder i denne controller
    @Autowired
    private UseCase useCase; //private UseCase useCase: Deklarerer en privat variabel useCase af typen UseCase
                             // som vil blive brugt til at kalde forretningslogik-metoder.

    //Denne annotation specificerer, at metoden skal håndtere GET-forespørgsler til URL'en "/add-ingredients".
    @GetMapping("/add-ingredients")
    public String showAddIngredientsForm() {
        return "add-ingredients";
    }

    //Denne annotation specificerer, at metoden skal håndtere POST-forespørgsler til URL'en "/add-ingredients".
    @PostMapping("/add-ingredients")
    public String addIngredient(@RequestParam("name") String name,               //Metoden tager flere parametre som markeret med @RequestParam, hentes fra POST
                                @RequestParam("fat") double fat,
                                @RequestParam("carbohydrate") double carbohydrate,
                                @RequestParam("protein") double protein,
                                @RequestParam("calories") int calories) {
        Ingredient ingredient = new Ingredient(); //Opretter en ny instans af klassen Ingredient
        ingredient.setName(name);      //Bliver sat med værdien fra POST-forespørgslen
        ingredient.setFat(fat);         //Bliver sat med værdien fra POST-forespørgslen
        ingredient.setCarbohydrate(carbohydrate);   //Bliver sat med værdien fra POST-forespørgslen
        ingredient.setProtein(protein);         //osv
        ingredient.setCalories(calories);       //osv
        useCase.createIngredients(ingredient);  //osv
        return "redirect:/add-ingredients";     //Returnerer en omdirigering til URL'en "/add-ingredients"
    }
}
