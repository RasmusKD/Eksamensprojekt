package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Model.Subscription;
import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final DBRepository dBRepository;
    private User currentUser;

    // Konstruktor til dependency injection af repository
    @Autowired
    public UseCase(DBRepository dBRepository) {
        this.dBRepository = dBRepository;
    }

    // Henter alle opskrifter fra databasen
    public List<Recipe> getAllRecipes() {
        return dBRepository.getAllRecipes();
    }

    // Henter en specifik opskrift ved dens ID
    public Recipe getRecipeById(int id) {
        return dBRepository.getRecipeById(id);
    }

    // Henter opskrifter baseret på dag
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
                currentUser = null;
                System.out.println("User deleted successfully.");
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

        if (currentUser != null && currentUser.getUserId() == userId) {
            currentUser.setSubscriber(subscriberStatus);
            reauthenticateCurrentUser();
        }
    }

    private void reauthenticateCurrentUser() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                currentUser, currentUser.getPassword(), currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
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
            bmr = (10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) + 5;
        } else {
            bmr = (10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) - 161;
        }

        double activityMultiplier = switch (user.getActivityLevel()) {
            case 1 -> 1.5;
            case 2 -> 1.7;
            case 3 -> 1.9;
            case 4 -> 2.4;
            default -> 1.2;
        };

        double totalCalories = bmr * activityMultiplier;

        switch (user.getGoal()) {
            case 0 -> totalCalories -= 500;
            case 1 -> totalCalories += 500;
            case 2 -> totalCalories += 300;
        }

        return totalCalories;
    }

    // Henter kalorier for et specifikt måltid
    public double getCaloriesForMeal(double totalCalories, String mealTime) {
        return switch (mealTime) {
            case "Breakfast" -> totalCalories * 0.4;
            case "Lunch", "Dinner" -> totalCalories * 0.3;
            default -> 0;
        };
    }

    // Justerer ingrediensmængder baseret på justerede kalorier
    public List<Ingredient> getAdjustedIngredients(Recipe recipe, double adjustedCalories) {
        List<Ingredient> adjustedIngredients = new ArrayList<>();
        double totalCalories = recipe.getTotalCalories();
        double adjustmentFactor = adjustedCalories / totalCalories;

        for (Ingredient ingredient : recipe.getIngredients()) {
            Ingredient adjustedIngredient = new Ingredient();
            adjustedIngredient.setName(ingredient.getName());
            adjustedIngredient.setFat(ingredient.getFat() * adjustmentFactor);
            adjustedIngredient.setProtein(ingredient.getProtein() * adjustmentFactor);
            adjustedIngredient.setCarbohydrate(ingredient.getCarbohydrate() * adjustmentFactor);
            adjustedIngredient.setCalories((int) (ingredient.getCalories() * adjustmentFactor));
            adjustedIngredient.setQuantity(ingredient.getQuantity() * adjustmentFactor); // Justerer mængden
            adjustedIngredients.add(adjustedIngredient);
        }
        return adjustedIngredients;
    }

    // Henter en opskrift ved dens ID med justerede kalorier baseret på brugerens behov
    public Recipe getRecipeByIdWithAdjustedCalories(int id, User user) {
        Recipe recipe = dBRepository.getRecipeById(id);
        double userCaloricNeeds = calculateCalories(user);
        double adjustedCalories = getCaloriesForMeal(userCaloricNeeds, recipe.getMealTime());
        recipe.setAdjustedCalories((int) adjustedCalories);
        recipe.setIngredients(getAdjustedIngredients(recipe, adjustedCalories));
        return recipe;
    }

    // Henter opskrifter baseret på dag med justerede kalorier baseret på brugerens behov
    public List<Recipe> getRecipesByDayWithAdjustedCalories(String day, User user) {
        List<Recipe> recipes = getRecipesByDay(day);
        double totalCalories = calculateCalories(user);

        for (Recipe recipe : recipes) {
            double adjustedCalories = getCaloriesForMeal(totalCalories, recipe.getMealTime());
            recipe.setAdjustedCalories((int) adjustedCalories);
        }

        return recipes;
    }

    // Beregner justerede kalorier for et måltid baseret på brugerens kaloribehov og måltidstidspunkt
    public double calculateAdjustedCalories(double userCaloricNeeds, String mealTime) {
        return getCaloriesForMeal(userCaloricNeeds, mealTime);
    }

    // Henter et abonnement ved bruger ID
    public Subscription getSubscriptionByUserId(int userId) {
        return dBRepository.getSubscriptionByUserId(userId);
    }

    // Sletter inaktive abonnementer for en bruger baseret på deres ID
    public void deleteInactiveSubscriptionsByUserId(int userId) {
        dBRepository.deleteInactiveSubscriptionsByUserId(userId);
    }

    public void updateRecipeWithIngredients(Recipe recipe, List<Integer> ingredientIds, List<Double> quantities) {
        dBRepository.updateRecipeWithIngredients(recipe, ingredientIds, quantities);
    }

    public boolean deleteRecipeById(int id) {
        return dBRepository.deleteRecipeById(id);
    }

    public void updateIngredient(Ingredient ingredient) {
        dBRepository.updateIngredient(ingredient);
    }

    public boolean deleteIngredientById(int id) {
        return dBRepository.deleteIngredientById(id);
    }

}
