package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Subscription;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    private UseCase useCase;

    @PostMapping("/create-checkout-session")
    public Map<String, String> createCheckoutSession(@RequestBody Map<String, Object> payload) {
        Map<String, String> response = new HashMap<>();
        try {
            String priceId = (String) payload.get("priceId");
            String userIdStr = (String) payload.get("userId"); // User ID received as String
            int userId = Integer.parseInt(userIdStr); // Convert String to Integer

            String email = useCase.getUserById(userId).getEmail();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setSuccessUrl("http://localhost:8080/stripe/payment-success?userId=" + userId) // Include userId in the success URL
                    .setCancelUrl("http://localhost:8080/payment-cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPrice(priceId)
                                    .setQuantity(1L)
                                    .build()
                    )
                    .setCustomerEmail(email)
                    .build();

            Session session = Session.create(params);
            response.put("id", session.getId());
        } catch (StripeException e) {
            response.put("error", e.getMessage());
        } catch (NumberFormatException e) {
            response.put("error", "Invalid user ID format.");
        }
        return response;
    }

    @GetMapping("/payment-success")
    public String handlePaymentSuccess(@RequestParam("userId") int userId) {
        Subscription subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
        subscription.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusMonths(1)));
        subscription.setPrice(9.99); // Example price
        subscription.setStatus("active");

        useCase.createSubscription(subscription);

        // Update the user's subscription status
        useCase.updateUserSubscriptionStatus(userId, true);

        return "redirect:/payment-success";
    }
}
