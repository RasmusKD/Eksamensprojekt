package com.minkostplan.eksamensprojekt;

import com.minkostplan.eksamensprojekt.Model.Ingredients;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EksamensprojektApplication {

	@Autowired
	private UseCase useCase;  // Autowire UserService

	public static void main(String[] args) {
		SpringApplication.run(EksamensprojektApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo() {
		return args -> {
			// Create ingredient data
			Ingredients newIngredient = new Ingredients();
			newIngredient.setName("Æble");
			newIngredient.setFat(10.0);
			newIngredient.setCarbohydrate(20.0);
			newIngredient.setProtein(30.0);

			// Call createIngredients method on the autowired userService instance
			useCase.createIngredients(newIngredient);
			System.out.println("New ingredient created successfully!");
		};
	}


}

