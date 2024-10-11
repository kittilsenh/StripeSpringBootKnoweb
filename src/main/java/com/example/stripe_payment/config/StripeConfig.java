package com.example.stripe_payment.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class StripeConfig {

    @Value("${stripe.api.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = this.secretKey;
    }
}