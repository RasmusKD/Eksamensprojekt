package com.minkostplan.eksamensprojekt;

import com.minkostplan.eksamensprojekt.Model.Ingredients;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class EksamensprojektApplication {

	@Autowired
	private UseCase useCase;  // Autowire UserService

	public static void main(String[] args) {
		SpringApplication.run(EksamensprojektApplication.class, args);
	}

	/*
		@Bean
		public CommandLineRunner demo() {
			return args -> {
				// Create ingredient data
				Ingredients newIngredient = new Ingredients();
				newIngredient.setName("Gulerod");
				newIngredient.setFat(10.0);
				newIngredient.setCarbohydrate(20.0);
				newIngredient.setProtein(30.0);

				// Call createIngredients method on the autowired userService instance
				useCase.createIngredients(newIngredient);
				System.out.println("New ingredient created successfully!");
			};
		}

	@Bean
	public CommandLineRunner demo() {
		return args -> {
			// Fetch all ingredients from the database
			List<Ingredients> allIngredients = useCase.getAllIngredients();

			// Create a new recipe
			Recipe recipe = new Recipe();
			recipe.setTitle("Test Recipe");
			recipe.setDescription("This is a test recipe created from existing ingredients.");
			recipe.setMethod("Mix ingredients together.");
			recipe.setCookingTime("10 minutes");
			recipe.setImageUrl("https://example.com/image.jpg");

			// Set ingredients for the recipe
			recipe.setIngredients(allIngredients);

			// Call the useCase method to create the recipe with ingredients
			useCase.createRecipeWithIngredients(recipe);

			System.out.println("Recipe created successfully with existing ingredients!");
		};
	}*/

}





