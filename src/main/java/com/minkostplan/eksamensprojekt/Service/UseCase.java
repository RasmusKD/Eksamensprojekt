package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Model.Ingredient;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Model.Subscription;
import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviceklasse, der håndterer forretningslogik og interaktion med DBRepository.
 */
@Service
public class UseCase {
    private final DBRepository dBRepository;
    private User currentUser;

    @Autowired
    public UseCase(DBRepository dBRepository) {
        this.dBRepository = dBRepository;
    }

    /**
     * Henter alle opskrifter fra databasen.
     *
     * @return en liste over alle opskrifter.
     */
    public List<Recipe> getAllRecipes() {
        return dBRepository.getAllRecipes();
    }

    /**
     * Henter en specifik opskrift med dens ID.
     *
     * @param id opskriftens ID.
     * @return den fundne opskrift.
     */
    public Recipe getRecipeById(int id) {
        return dBRepository.getRecipeById(id);
    }

    /**
     * Henter opskrifter baseret på uge.
     *
     * @param week ugen, hvor opskrifterne skal hentes til.
     * @return en liste over opskrifter for den angivne dag.
     */

    public List<Recipe> getRecipesByWeek(String week) {
        return dBRepository.getRecipesByWeek(week);
    }

    /**
     * Sætter den aktuelle bruger.
     *
     * @param user den bruger, der skal sættes som den aktuelle bruger.
     */

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    /**
     * Opretter en ny bruger i databasen.
     *
     * @param user den bruger, der skal oprettes.
     */
    public void createUser(User user) {
        dBRepository.createUser(user);
    }

    /**
     * Opdaterer en eksisterende bruger i databasen.
     *
     * @param user den bruger, der skal opdateres.
     */
    public void updateUser(User user) {
        dBRepository.updateUser(user);
    }

    /**
     * Henter en bruger med deres ID.
     *
     * @param userId brugerens ID.
     * @return den fundne bruger.
     */
    public User getUserById(int userId) {
        return dBRepository.getUserById(userId);
    }

    /**
     * Opdaterer ansættelsesstatus for en bruger baseret på deres email.
     *
     * @param email            brugerens email.
     * @param employmentStatus den nye ansættelsesstatus.
     */
    public void updateEmploymentStatus(String email, int employmentStatus) {
        dBRepository.updateEmploymentStatus(email, employmentStatus);
    }

    /**
     * Sletter den aktuelle bruger.
     */
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

    /**
     * Opretter en ny ingrediens i databasen.
     *
     * @param ingredient den ingrediens, der skal oprettes.
     */
    public void createIngredients(Ingredient ingredient) {
        dBRepository.createIngredients(ingredient);
        System.out.println("New ingredient created successfully!");
    }

    /**
     * Henter alle ingredienser fra databasen.
     *
     * @return en liste over alle ingredienser.
     */
    public List<Ingredient> getAllIngredients() {
        return dBRepository.getAllIngredients();
    }

    /**
     * Henter en specifik ingrediens med dens ID.
     *
     * @param ingredientId ingrediensens ID.
     * @return den fundne ingrediens.
     */
    public Ingredient getIngredientById(int ingredientId) {
        return dBRepository.getIngredientById(ingredientId);
    }

    /**
     * Opretter en ny opskrift med tilhørende ingredienser og mængder.
     *
     * @param recipe        opskriften, der skal oprettes.
     * @param ingredientIds liste over ingrediens IDs.
     * @param quantities    liste over mængde af hver ingrediens.
     */
    public void createRecipeWithIngredients(Recipe recipe, List<Integer> ingredientIds, List<Double> quantities) {
        dBRepository.createRecipeWithIngredients(recipe, ingredientIds, quantities);
    }

    /**
     * Opretter et nyt abonnement i databasen.
     *
     * @param subscription det abonnement, der skal oprettes.
     */
    public void createSubscription(Subscription subscription) {
        dBRepository.createSubscription(subscription);
    }

    /**
     * Opdaterer abonnementsstatus for en bruger baseret på deres ID.
     *
     * @param userId           brugerens ID.
     * @param subscriberStatus den nye abonnementsstatus.
     */
    public void updateUserSubscriptionStatus(int userId, boolean subscriberStatus) {
        dBRepository.updateUserSubscriptionStatus(userId, subscriberStatus);

        if (currentUser != null && currentUser.getUserId() == userId) {
            currentUser.setSubscriber(subscriberStatus);
            reauthenticateCurrentUser();
        }
    }

    /**
     * Genautentificerer den aktuelle bruger efter opdatering af deres abonnementsstatus.
     */
    private void reauthenticateCurrentUser() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                currentUser, currentUser.getPassword(), currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Opdaterer status for et abonnement baseret på dets ID.
     *
     * @param subscriptionId abonnements ID.
     * @param status         den nye status for abonnementet.
     */
    public void updateSubscriptionStatus(String subscriptionId, String status) {
        dBRepository.updateSubscriptionStatus(subscriptionId, status);
    }

    /**
     * Henter en bruger med deres email.
     *
     * @param email brugerens email.
     * @return den fundne bruger.
     */
    public User getUserByEmail(String email) {
        return dBRepository.findByEmail(email);
    }

    /**
     * Beregner det daglige kaloriebehov for en bruger.
     *
     * @param user den bruger kalorierne skal beregnes for.
     * @return det daglige kaloriebehov.
     */
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

    /**
     * Henter kalorier for et specifikt måltid.
     *
     * @param totalCalories brugerens samlede kaloriebehov.
     * @param mealTime      måltidstidspunktet.
     * @return kalorierne for det specifikke måltid.
     */
    public double getCaloriesForMeal(double totalCalories, String mealTime) {
        return switch (mealTime) {
            case "Breakfast" -> totalCalories * 0.4;
            case "Lunch", "Dinner" -> totalCalories * 0.3;
            default -> 0;
        };
    }

    /**
     * Justerer ingrediensmængder baseret på justerede kalorier.
     *
     * @param recipe           opskriften, der skal justeres.
     * @param adjustedCalories de justerede kalorier.
     * @return en liste over justerede ingredienser.
     */
    public List<Ingredient> getAdjustedIngredients(Recipe recipe, double adjustedCalories) {
        List<Ingredient> adjustedIngredients = new ArrayList<>();
        double totalCalories = recipe.getTotalCalories();
        double adjustmentFactor = adjustedCalories / totalCalories;

        for (Ingredient ingredient : recipe.getIngredients()) {
            Ingredient adjustedIngredient = new Ingredient();
            adjustedIngredient.setIngredientId(ingredient.getIngredientId()); // Ensure ID is copied
            adjustedIngredient.setName(ingredient.getName());
            adjustedIngredient.setFat(ingredient.getFat() * adjustmentFactor);
            adjustedIngredient.setProtein(ingredient.getProtein() * adjustmentFactor);
            adjustedIngredient.setCarbohydrate(ingredient.getCarbohydrate() * adjustmentFactor);
            adjustedIngredient.setCalories((int) (ingredient.getCalories() * adjustmentFactor));
            adjustedIngredient.setQuantity(ingredient.getQuantity() * adjustmentFactor); // Adjust quantity
            adjustedIngredients.add(adjustedIngredient);
        }
        return adjustedIngredients;
    }



    /**
     * Henter opskrifter baseret på dag med justerede kalorier baseret på brugerens behov.
     *
     * @param week dagen, hvor opskrifterne skal hentes for.
     * @param user den bruger, hvis behov skal tages i betragtning.
     * @return en liste over justerede opskrifter.
     */
    public List<Recipe> getRecipesByWeekWithAdjustedCalories(String week, User user) {
        List<Recipe> recipes = getRecipesByWeek(week);
        double totalCalories = calculateCalories(user);

        for (Recipe recipe : recipes) {
            double adjustedCalories = getCaloriesForMeal(totalCalories, recipe.getMealTime());
            recipe.setAdjustedCalories((int) adjustedCalories);
        }

        return recipes;
    }

    /**
     * Beregner justerede kalorier for et måltid baseret på brugerens kaloriebehov og måltidstidspunkt.
     *
     * @param userCaloricNeeds brugerens samlede kaloriebehov.
     * @param mealTime         måltidstidspunktet.
     * @return de justerede kalorier for måltidet.
     */
    public double calculateAdjustedCalories(double userCaloricNeeds, String mealTime) {
        return getCaloriesForMeal(userCaloricNeeds, mealTime);
    }

    /**
     * Henter et abonnement med bruger ID.
     *
     * @param userId brugerens ID.
     * @return det fundne abonnement.
     */
    public Subscription getLatestSubscriptionByUserId(int userId) {
        return dBRepository.getLatestSubscriptionByUserId(userId);
    }

    /**
     * Sletter inaktive abonnementer for en bruger baseret på deres ID.
     *
     * @param userId brugerens ID.
     */
    @Deprecated
    public void deleteInactiveSubscriptionsByUserId(int userId) {
        dBRepository.deleteInactiveSubscriptionsByUserId(userId);
    }

    /**
     * Opdaterer en opskrift med tilhørende ingredienser og mængder.
     *
     * @param recipe        opskriften, der skal opdateres.
     * @param ingredientIds liste over ingrediens IDs.
     * @param quantities    liste over mængder for hver ingrediens.
     */
    public void updateRecipeWithIngredients(Recipe recipe, List<Integer> ingredientIds, List<Double> quantities) {
        dBRepository.updateRecipeWithIngredients(recipe, ingredientIds, quantities);
    }

    /**
     * Sletter en opskrift med dens ID.
     *
     * @param id opskriftens ID.
     * @return true hvis opskriften blev slettet, ellers false.
     */
    public boolean deleteRecipeById(int id) {
        return dBRepository.deleteRecipeById(id);
    }

    /**
     * Opdaterer en ingrediens i databasen.
     *
     * @param ingredient den ingrediens, der skal opdateres.
     */
    public void updateIngredient(Ingredient ingredient) {
        dBRepository.updateIngredient(ingredient);
    }

    /**
     * Sletter en ingrediens med dens ID.
     *
     * @param id ingrediensens ID.
     * @return true hvis ingrediensen blev slettet, ellers false.
     */
    public boolean deleteIngredientById(int id) {
        return dBRepository.deleteIngredientById(id);
    }

    public void addIngredientsToShoppingList(User user, List<Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            dBRepository.addIngredientToShoppingList(user.getUserId(), ingredient);
        }
    }



    public List<Ingredient> getShoppingList(int userId) {
        return dBRepository.getShoppingListByUserId(userId);
    }

    public void addFavoriteRecipe(int userId, int recipeId) {
        // Call the method in your DBRepository to add the favorite recipe to the database
        dBRepository.addFavoriteRecipe(userId, recipeId);
    }
}
