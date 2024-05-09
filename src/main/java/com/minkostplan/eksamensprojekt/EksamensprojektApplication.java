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
/*
	@Bean
	public CommandLineRunner demo() {
		return args -> {
			// Create user data
			User newUser = new User(
					3, "Søren", "Clausen", "Søren.Clausen@gmail.com", "123", 22, 'M', 76.0, 188.0, "Active", true, false
			);

			// Call createUser method to insert the user into the database
			userService.createUser(newUser);
			System.out.println("New user created successfully!");

		};
	}
*/

	@Bean
	public CommandLineRunner demo() {
		return args -> {
			// Attempt to log in with the same credentials
			System.out.println("Attempting to log in...");
			User loggedInUser = userService.login("Peter.Mortensen@gmail.com", "123");

			if (loggedInUser != null) {
				System.out.println("Login successful for: " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName());

				// Check if user is logged in
				if (userService.isUserLoggedIn()) {
					System.out.println("User is logged in, proceeding to logout...");
					userService.logout();  // Simulate logout
				}
			} else {
				System.out.println("Login failed.");
			}
		};
	}


}
