package com.example.stripe_payment.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.example.stripe_payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent() {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", 5000); // Amount in cents ($50.00)
            params.put("currency", "usd");
            params.put("payment_method_types", java.util.List.of("card"));

            PaymentIntent paymentIntent = paymentService.createPaymentIntent(params);

            Map<String, String> responseData = new HashMap<>();
            responseData.put("clientSecret", paymentIntent.getClientSecret());

            return ResponseEntity.ok(responseData);

        } catch (StripeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
