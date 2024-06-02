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

/**
 * Controller-klasse til håndtering af Stripe-betalinger og abonnementer.
 */
@RestController
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    private UseCase useCase;

    /**
     * Opretter en Stripe Checkout Session.
     *
     * @param payload Payload med pris-ID og bruger-ID.
     * @return Map med session ID eller fejlmeddelelse.
     */
    @PostMapping("/create-checkout-session")
    public Map<String, String> createCheckoutSession(@RequestBody Map<String, Object> payload) {
        Map<String, String> response = new HashMap<>();
        try {
            String priceId = (String) payload.get("priceId");
            String userIdStr = (String) payload.get("userId");
            int userId = Integer.parseInt(userIdStr);

            String email = useCase.getUserById(userId).getEmail();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setSuccessUrl("http://localhost:8080/stripe/payment-success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl("http://localhost:8080/payment-cancel")
                    .setClientReferenceId(userIdStr)
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

    /**
     * Håndterer betalingens succes.
     *
     * @param sessionId Stripe session ID.
     * @return ModelAndView med succesmeddelelse.
     */
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
                subscription.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(31)));
                subscription.setPrice(196);
                subscription.setStatus("active");
                subscription.setSubscriptionId(subscriptionId);

                useCase.createSubscription(subscription);

                useCase.updateUserSubscriptionStatus(userId, true);

                modelAndView.addObject("message", "Betaling gennemført! Tak for din betaling. Dit abonnement er nu aktivt.");
            } else {
                modelAndView.addObject("message", "Der opstod en fejl ved oprettelse af abonnement.");
            }
        } catch (StripeException e) {
            modelAndView.addObject("message", "Fejl ved hentning af betalingssession: " + e.getMessage());
        }

        return modelAndView;
    }

    /**
     * Annullerer et abonnement.
     *
     * @param payload Payload med abonnement-ID.
     * @return Map med status eller fejlmeddelelse.
     */
    @PostMapping("/cancel-subscription")
    public Map<String, String> cancelSubscription(@RequestBody Map<String, Object> payload) {
        Map<String, String> response = new HashMap<>();
        try {
            String subscriptionId = (String) payload.get("subscriptionId");
            System.out.println("Received subscriptionId: " + subscriptionId);
            com.stripe.model.Subscription subscription = com.stripe.model.Subscription.retrieve(subscriptionId);

            if (subscription == null) {
                response.put("error", "Subscription not found.");
                return response;
            }

            SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                    .setCancelAtPeriodEnd(true)
                    .build();

            subscription.update(params);

            useCase.updateSubscriptionStatus(subscriptionId, "inactive");

            response.put("status", "subscription_cancelled");
        } catch (StripeException e) {
            response.put("error", e.getMessage());
        } catch (IllegalArgumentException e) {
            response.put("error", "Invalid subscriptionId format.");
        }
        return response;
    }

    /**
     * Opdaterer abonnementsstatus dagligt.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateSubscriptionStatus() {
        try {
            com.stripe.param.SubscriptionListParams params = com.stripe.param.SubscriptionListParams.builder()
                    .setLimit(100L)
                    .build();

            SubscriptionCollection subscriptions = com.stripe.model.Subscription.list(params);

            for (com.stripe.model.Subscription stripeSubscription : subscriptions.getData()) {
                Map<String, String> metadata = stripeSubscription.getMetadata();
                if (metadata.containsKey("userId")) {
                    int userId = Integer.parseInt(metadata.get("userId"));

                    if (stripeSubscription.getCancelAtPeriodEnd() && stripeSubscription.getCurrentPeriodEnd() <= Instant.now().getEpochSecond()) {
                        useCase.updateUserSubscriptionStatus(userId, false);
                    } else if (stripeSubscription.getCurrentPeriodEnd() <= Instant.now().getEpochSecond()) {
                        SubscriptionUpdateParams updateParams = SubscriptionUpdateParams.builder()
                                .setCancelAtPeriodEnd(false)
                                .build();
                        stripeSubscription.update(updateParams);

                        Subscription subscription = useCase.getLatestSubscriptionByUserId(userId);
                        subscription.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusDays(7)));
                        useCase.createSubscription(subscription);

                        useCase.updateUserSubscriptionStatus(userId, true);
                    }
                } else {
                    System.out.println("Metadata does not contain userId for subscription: " + stripeSubscription.getId());
                }
            }
        } catch (StripeException e) {
            e.printStackTrace();
        }
    }
}
