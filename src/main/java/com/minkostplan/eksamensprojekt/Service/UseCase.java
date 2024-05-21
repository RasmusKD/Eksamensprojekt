package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Model.Ingredients;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Model.Subscription;
import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UseCase {

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    private DBRepository dBRepository;

    @Autowired
    public UseCase(DBRepository dBRepository) {
        this.dBRepository = dBRepository;
    }

    private User currentUser;



    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void createUser(User user) {
        dBRepository.createUser(user);
    }

    public void updateUser(User user) {
        dBRepository.updateUser(user);
    }


    public User getUserById(int userId) {
        return dBRepository.getUserById(userId);
    }

    public boolean isUserLoggedIn() {
        return this.currentUser != null;
    }
    public void updateEmploymentStatus(String email, int employmentStatus) {
        dBRepository.updateEmploymentStatus(email, employmentStatus);
    }


    public void deleteUser() {
        if (currentUser != null) {
            if (dBRepository.deleteUser(currentUser.getUserId())) {
                System.out.println("User deleted successfully.");
                currentUser = null;
            } else {
                System.out.println("Failed to delete user.");
            }
        } else {
            System.out.println("No user currently logged in.");
        }
    }

    public void createIngredients(Ingredients ingredients) {
        dBRepository.createIngredients(ingredients);
        System.out.println("New ingredient created successfully!");
    }
/*
    public void createRecipeWithIngredients(Recipe recipe) {
        dBRepository.createRecipeWithIngredients(recipe, recipe.getIngredients());
    }*/

    public List<Ingredients> getAllIngredients() {
        return dBRepository.getAllIngredients();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void createSubscription(Subscription subscription) {
        dBRepository.createSubscription(subscription);
    }

    public void updateUserSubscriptionStatus(int userId, boolean subscriberStatus) {
        dBRepository.updateUserSubscriptionStatus(userId, subscriberStatus);
    }

    public User getUserByEmail(String email) {
        return dBRepository.findByEmail(email);
    }



    public String getSuccessUrl() {
        return successUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public double calculateCalories(User user) {
        double bmr;
        if (user.getGender() == 'M') {
            bmr = 88.362 + (13.397 * user.getWeight()) + (4.799 * user.getHeight()) - (5.677 * user.getAge());
        } else {
            bmr = 447.593 + (9.247 * user.getWeight()) + (3.098 * user.getHeight()) - (4.330 * user.getAge());
        }

        double activityMultiplier;
        switch (user.getActivityLevel()) {
            case 0:
                activityMultiplier = 1.2;
                break;
            case 1:
                activityMultiplier = 1.375;
                break;
            case 2:
                activityMultiplier = 1.55;
                break;
            case 3:
                activityMultiplier = 1.725;
                break;
            case 4:
                activityMultiplier = 1.9;
                break;
            default:
                activityMultiplier = 1.2; // Default to sedentary if activity level is unknown
        }

        double totalCalories = bmr * activityMultiplier;

        switch (user.getGoal()) {
            case 0: // Vægttab
                totalCalories -= 500;
                break;
            case 1: // Vægtøgning
                totalCalories += 500;
                break;
            case 2: // Muskelopbygning
                totalCalories += 300; // Slight surplus for muscle gain
                break;
            case 3: // Vedligehold vægt
                // No adjustment needed
                break;
        }

        return totalCalories;
    }
}
