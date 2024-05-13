package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Model.Ingredients;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import com.minkostplan.eksamensprojekt.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UseCase {
    private static DBRepository dBRepository; // Make it static

    @Autowired
    public UseCase(DBRepository dBRepository) {
        UseCase.dBRepository = dBRepository; // Assign it in constructor
    }

    private User currentUser; // Holds the reference to the logged-in user

    public void createUser(User user) {
        dBRepository.createUser(user);
    }

    public User login(String email, String password) {
        currentUser = dBRepository.login(email, password);  // Set currentUser
        return currentUser;
    }

    public void logout() {
        if (currentUser != null) {
            System.out.println("Logging out user: " + currentUser.getEmail());
            currentUser = null;
        } else {
            System.out.println("No user currently logged in.");
        }
        System.out.println("User has been logged out.");
    }

    public void updateUser(User user) {
        if (currentUser != null) {
            // Make sure the user is logged in
            user.setUserId(currentUser.getUserId()); // Ensure the ID remains the same
            dBRepository.updateUser(user);
            System.out.println("User information updated successfully.");
        } else {
            System.out.println("No user currently logged in. Unable to update user information.");
        }
    }

    public User getUserById(int userId) {
        return dBRepository.getUserById(userId);
    }

    public boolean isUserLoggedIn() {
        return this.currentUser != null;
    }

    public void deleteUser() {
        if (currentUser != null) {
            if (dBRepository.deleteUser(currentUser.getUserId())) {
                System.out.println("User deleted successfully.");
                currentUser = null; // Clear the current user since the account is deleted
            } else {
                System.out.println("Failed to delete user.");
            }
        } else {
            System.out.println("No user currently logged in.");
        }
    }

    public void createIngredients(Ingredients ingredients) {
        // Delegate to DBRepository to create ingredients
        dBRepository.createIngredients(ingredients);
        System.out.println("New ingredient created successfully!");
    }

    public void createRecipeWithIngredients(Recipe recipe) {
        // Now, you can save the recipe to the database
        // You need to implement the method in DBRepository to handle this
        // Call the repository method to create the recipe with ingredients
        dBRepository.createRecipeWithIngredients(recipe, recipe.getIngredients());
    }


    public List<Ingredients> getAllIngredients() {
        // Delegate to DBRepository to fetch all ingredients from the database
        return dBRepository.getAllIngredients();
    }


}
