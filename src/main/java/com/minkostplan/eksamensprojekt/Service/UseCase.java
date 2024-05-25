package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Model.Subscription;
import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UseCase {

    // Injicerer Stripe success URL fra applikationsindstillingerne
    @Value("${stripe.success.url}")
    private String successUrl;

    // Injicerer Stripe cancel URL fra applikationsindstillingerne
    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    // Reference til database repository
    private DBRepository dBRepository;

    // Konstruktor til dependency injection af repository
    @Autowired
    public UseCase(DBRepository dBRepository) {
        this.dBRepository = dBRepository;
    }

    private User currentUser;

    // Henter alle opskrifter fra databasen
    public List<Recipe> getAllRecipes() {
        return dBRepository.getAllRecipes(); // Implement this method in your repository
    }

    // Henter en specifik opskrift ved dens ID
    public Recipe getRecipeById(int id) {
        return dBRepository.getRecipeById(id); // Implement this method in your repository
    }

    public List<Recipe> getRecipesByDay(String day) {
        return dBRepository.getRecipesByDay(day);
    }

    // Sætter eller vælger den aktuelle bruger
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    // Opretter en ny bruger i databasen
    public void createUser(User user) {
        dBRepository.createUser(user);
    }

    // Opdaterer en eksisterende bruger i databasen
    public void updateUser(User user) {
        dBRepository.updateUser(user);
    }

    // Henter en bruger ved deres ID
    public User getUserById(int userId) {
        return dBRepository.getUserById(userId);
    }

    // Kontrollerer om en bruger er logget ind
    public boolean isUserLoggedIn() {
        return this.currentUser != null;
    }

    // Opdaterer ansættelsesstatus for en bruger baseret på deres email
    public void updateEmploymentStatus(String email, int employmentStatus) {
        dBRepository.updateEmploymentStatus(email, employmentStatus);
    }

    // Sletter den aktuelt loggede bruger
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

    // Opretter en ny ingrediens i databasen
    public void createIngredients(Ingredient ingredient) {
        dBRepository.createIngredients(ingredient);
        System.out.println("New ingredient created successfully!");
    }

    // Henter alle ingredienser fra databasen
    public List<Ingredient> getAllIngredients() {
        return dBRepository.getAllIngredients();
    }

    // Henter en specifik ingrediens ved dens ID
    public Ingredient getIngredientById(int ingredientId) {
        return dBRepository.getIngredientById(ingredientId);
    }

    // Opretter en ny opskrift med tilhørende ingredienser og mængder
    public void createRecipeWithIngredients(Recipe recipe, List<Integer> ingredientIds, List<Double> quantities) {
        dBRepository.createRecipeWithIngredients(recipe, ingredientIds, quantities);
    }

    // Henter den aktuelt loggede bruger
    public User getCurrentUser() {
        return currentUser;
    }

    // Opretter et nyt abonnement i databasen
    public void createSubscription(Subscription subscription) {
        dBRepository.createSubscription(subscription);
    }

    // Opdaterer abonnementsstatus for en bruger baseret på deres ID
    public void updateUserSubscriptionStatus(int userId, boolean subscriberStatus) {
        dBRepository.updateUserSubscriptionStatus(userId, subscriberStatus);
    }

    // Opdaterer status for et abonnement baseret på dets ID
    public void updateSubscriptionStatus(String subscriptionId, String status) {
        dBRepository.updateSubscriptionStatus(subscriptionId, status);
    }

    // Henter en bruger ved deres email
    public User getUserByEmail(String email) {
        return dBRepository.findByEmail(email);
    }

    // Henter Stripe success URL
    public String getSuccessUrl() {
        return successUrl;
    }

    // Henter Stripe cancel URL
    public String getCancelUrl() {
        return cancelUrl;
    }

    // Beregner det daglige kaloriebehov for en bruger
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

    // Henter et abonnement ved bruger ID
    public Subscription getSubscriptionByUserId(int userId) {
        return dBRepository.getSubscriptionByUserId(userId);
    }

    // Sletter inaktive abonnementer for en bruger baseret på deres ID
    public void deleteInactiveSubscriptionsByUserId(int userId) {
        dBRepository.deleteInactiveSubscriptionsByUserId(userId);
    }
}
