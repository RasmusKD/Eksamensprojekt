package com.minkostplan.eksamensprojekt.Controller;

import com.minkostplan.eksamensprojekt.Model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentPageController {

    @GetMapping("/stripe-payment")
    public String showPaymentPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            model.addAttribute("userId", user.getUserId());
        }
        return "stripe-payment";
    }

    @GetMapping("/payment-cancel")
    public String paymentCancel() {
        return "payment-cancel";
    }

    @GetMapping("/payment-success")
    public String paymentSuccess() {
        return "payment-success";
    }
}
