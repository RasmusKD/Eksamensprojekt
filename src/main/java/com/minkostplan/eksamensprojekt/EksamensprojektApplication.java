package com.minkostplan.eksamensprojekt;

import com.minkostplan.eksamensprojekt.Service.UseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EksamensprojektApplication {

	@Autowired
	private UseCase useCase;

	@Autowired
	private PasswordEncoder passwordEncoder;  // Autowire PasswordEncoder

	public static void main(String[] args) {
		SpringApplication.run(EksamensprojektApplication.class, args);
	}
/*
	@Bean
	public CommandLineRunner createUserWithIsEmployedTwo() {
		return args -> {
			// Create a new User object
			User newUser = new User();
			newUser.setFirstName("Simon");
			newUser.setLastName("Kostplan");
			newUser.setEmail("Simon@minkostplan.com");

			// Hash the password before setting it
			String hashedPassword = passwordEncoder.encode("123");
			newUser.setPassword(hashedPassword);

			newUser.setAge(20);
			newUser.setGender('M');
			newUser.setWeight(75.0);
			newUser.setHeight(180.0);
			newUser.setActivityLevel(1);
			newUser.setGoal(0);
			newUser.setEmployed(2); // Set isEmployed to 2
			newUser.setSubscriber(true);

			// Call the useCase method to create the user
			useCase.createUser(newUser);

			System.out.println("User created successfully with isEmployed set to 2!");
		};
	}
*/
	// Other CommandLineRunners or beans
}
