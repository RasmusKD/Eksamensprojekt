package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.User;
import com.minkostplan.eksamensprojekt.Service.UseCase;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping("/stripe")
public class StripeController {

    @Autowired
    private UseCase useCase;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @GetMapping("/stripe-payment")
    public String getStripePaymentPage(Model model) {
        User currentUser = useCase.getCurrentUser(); // Assuming you have a method to get the current user
        if (currentUser != null) {
            model.addAttribute("userId", currentUser.getUserId());
        }
        return "stripe-payment";
    }

    @PostMapping("/create-checkout-session")
    public @ResponseBody
    Session createCheckoutSession(@RequestBody CheckoutSessionRequest request) throws StripeException {
        return useCase.createCheckoutSession(request.getUserId(), request.getPriceId());
    }

    @PostMapping("/webhook")
    public String handleStripeEvent(HttpServletRequest request) {
        String payload;
        try {
            payload = request.getReader().lines()
                    .reduce("", (accumulator, actual) -> accumulator + actual);
        } catch (IOException e) {
            return "";
        }

        String sigHeader = request.getHeader("Stripe-Signature");
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return "";
        }

        // Handle the event
        switch (event.getType()) {
            case "checkout.session.completed":
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                if (session != null) {
                    useCase.handleCheckoutSessionCompleted(session);
                }
                break;
            // ... handle other event types
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return "";
    }

    // DTO for request body
    public static class CheckoutSessionRequest {
        private int userId;
        private String priceId;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getPriceId() {
            return priceId;
        }

        public void setPriceId(String priceId) {
            this.priceId = priceId;
        }
    }
}
