package com.example.stripe_payment.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.example.stripe_payment.model.Payment;
import com.example.stripe_payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${stripe.api.secret-key}")
    private String stripeApiSecretKey;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, Object>> createPaymentIntent(@RequestBody Map<String, String> data) {
        Stripe.apiKey = stripeApiSecretKey;

        String email = data.get("email");

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(5000L) // $50.00
                .setCurrency("usd")
                .setReceiptEmail(email)
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clientSecret", paymentIntent.getClientSecret());

            // Don't save the payment yet, only after confirmation
            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorData);
        }
    }


    @PostMapping("/update-payment-status")
    public ResponseEntity<String> updatePaymentStatus(@RequestBody Map<String, String> data) {
        String email = data.get("email");
        String status = data.get("status");

        // Save the payment only when we have a final status (Success or Fail)
        Payment payment = new Payment();
        payment.setEmail(email);
        payment.setDateTime(LocalDateTime.now());
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);

        System.out.println("Payment status for email " + email + " updated to " + status);
        return ResponseEntity.ok("Payment status updated");
    }

}