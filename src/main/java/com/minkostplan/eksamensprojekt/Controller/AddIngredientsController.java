package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Ingredients;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AddIngredientsController {

    @Autowired
    private UseCase useCase;

    @GetMapping("/add-ingredients")
    public String showAddIngredientsForm() {
        return "add-ingredients";
    }

    @PostMapping("/add-ingredients")
    public String addIngredient(@RequestParam("name") String name,
                                @RequestParam("fat") double fat,
                                @RequestParam("carbohydrate") double carbohydrate,
                                @RequestParam("protein") double protein) {
        Ingredients ingredient = new Ingredients();
        ingredient.setName(name);
        ingredient.setFat(fat);
        ingredient.setCarbohydrate(carbohydrate);
        ingredient.setProtein(protein);
        useCase.createIngredients(ingredient);
        return "redirect:/add-ingredients";
    }
}
