package com.minkostplan.eksamensprojekt;

import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EksamensprojektApplication {

	@Autowired
	private UserService userService;  // Autowire UserService

	public static void main(String[] args) {
		SpringApplication.run(EksamensprojektApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo() {
		return args -> {
			// Opret bruger data
			User newUser = new User(
					1, "John", "Doe", "john.doe@example.com", "password123", 30, 'M', 70.0, 180.0, "Active", true, false
			);

			// Kalder createUser metode for at indsætte brugeren i databasen
			userService.createUser(newUser);  // Use the autowired service instance
			System.out.println("New user created successfully!");
		};
	}
}
