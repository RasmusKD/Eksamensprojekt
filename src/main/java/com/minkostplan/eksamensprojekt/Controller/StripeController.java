package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.Subscription;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import com.stripe.exception.StripeException;
import com.stripe.model.SubscriptionCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.SubscriptionUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;
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
                    .setSuccessUrl("http://localhost:8080/stripe/payment-success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl("http://localhost:8080/payment-cancel")
                    .setClientReferenceId(userIdStr) // Set client_reference_id
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
    public ModelAndView handlePaymentSuccess(@RequestParam("session_id") String sessionId) {
        ModelAndView modelAndView = new ModelAndView("payment-success");
        try {
            Session session = Session.retrieve(sessionId);
            String subscriptionId = session.getSubscription();
            int userId = Integer.parseInt(session.getClientReferenceId());

            if (subscriptionId != null) {
                Subscription subscription = new Subscription();
                subscription.setUserId(userId);
                subscription.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
                subscription.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusMonths(1)));
                subscription.setPrice(9.99); // Example price
                subscription.setStatus("active");
                subscription.setSubscriptionId(subscriptionId); // Store the Stripe subscription ID

                useCase.createSubscription(subscription);

                // Update the user's subscription status and subscription ID
                useCase.updateUserSubscriptionStatus(userId, true, subscriptionId);

                modelAndView.addObject("message", "Betaling gennemført! Tak for din betaling. Dit abonnement er nu aktivt.");
            } else {
                modelAndView.addObject("message", "Der opstod en fejl ved oprettelse af abonnement.");
            }
        } catch (StripeException e) {
            modelAndView.addObject("message", "Fejl ved hentning af betalingssession: " + e.getMessage());
        }

        return modelAndView;
    }

    @PostMapping("/cancel-subscription")
    public Map<String, String> cancelSubscription(@RequestBody Map<String, Object> payload) {
        Map<String, String> response = new HashMap<>();
        try {
            String subscriptionId = (String) payload.get("subscriptionId");
            System.out.println("Received subscriptionId: " + subscriptionId); // Add this line for debugging
            com.stripe.model.Subscription subscription = com.stripe.model.Subscription.retrieve(subscriptionId);

            SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                    .setCancelAtPeriodEnd(true)
                    .build();

            subscription.update(params);

            response.put("status", "subscription_cancelled");
        } catch (StripeException e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void updateSubscriptionStatus() {
        try {
            com.stripe.param.SubscriptionListParams params = com.stripe.param.SubscriptionListParams.builder()
                    .setLimit(100L) // Cast the limit to Long
                    .build();

            SubscriptionCollection subscriptions = com.stripe.model.Subscription.list(params);

            for (com.stripe.model.Subscription stripeSubscription : subscriptions.getData()) {
                if (stripeSubscription.getCancelAtPeriodEnd() && stripeSubscription.getCurrentPeriodEnd() <= Instant.now().getEpochSecond()) {
                    int userId = Integer.parseInt(stripeSubscription.getMetadata().get("userId"));
                    useCase.updateUserSubscriptionStatus(userId, false);
                }
            }
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}
