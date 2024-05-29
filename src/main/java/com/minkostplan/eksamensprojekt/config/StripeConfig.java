package com.minkostplan.eksamensprojekt.config;

import com.stripe.Stripe;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Konfigurationsklasse til Stripe API-integration.
 */
@Configuration
public class StripeConfig {

    @Value("${stripe.api.key}")
    private String apiKey;

    /**
     * Initialiserer Stripe API-nøglen ved opstart.
     */
    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }
}
