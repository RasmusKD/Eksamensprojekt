package com.minkostplan.eksamensprojekt.Service;

import com.minkostplan.eksamensprojekt.Model.Ingredients;
import com.minkostplan.eksamensprojekt.Model.Recipe;
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

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

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

    public User getUserByEmail(String email) {
        return dBRepository.findByEmail(email);
    }

    public void createIngredients(Ingredients ingredients) {
        dBRepository.createIngredients(ingredients);
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
    public Session createCheckoutSession(int userId, String priceId) throws StripeException {
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                        .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl(cancelUrl)
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setPrice(priceId)
                                        .setQuantity(1L)
                                        .build()
                        )
                        .putMetadata("userId", String.valueOf(userId))
                        .build();

        return Session.create(params);
    }

    public void updateSubscriptionStatus(int userId, String status) {
        dBRepository.updateSubscriptionStatus(userId, status);
    }

    public void handleCheckoutSessionCompleted(Session session) {
        String userId = session.getMetadata().get("userId");
        updateSubscriptionStatus(Integer.parseInt(userId), "active");
    }
}
