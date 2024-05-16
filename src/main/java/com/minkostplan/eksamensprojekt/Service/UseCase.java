package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Model.Ingredients;
import com.minkostplan.eksamensprojekt.Model.Recipe;
import com.minkostplan.eksamensprojekt.Model.Subscription;
import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Repository.DBRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
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
        if (currentUser != null) {
            user.setUserId(currentUser.getUserId());
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

    public void createRecipeWithIngredients(Recipe recipe) {
        dBRepository.createRecipeWithIngredients(recipe, recipe.getIngredients());
    }

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


    public String getSuccessUrl() {
        return successUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }
}
