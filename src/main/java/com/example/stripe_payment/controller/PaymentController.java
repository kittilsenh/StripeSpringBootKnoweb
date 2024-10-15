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
import java.util.List;
import java.util.Map;

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

        // Create and save the new payment
        Payment payment = new Payment();
        payment.setEmail(email);
        payment.setDateTime(LocalDateTime.now());
        payment.setPaymentStatus(status);

        // Set account status based on payment status
        if (status.equals("Success")) {
            // Always set account status to "active" when payment is "Success"
            payment.setAccountStatus("active");
        } else if (status.equals("Fail")) {
            // Check for recent successful payments to decide if account stays active or inactive
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
            List<Payment> recentSuccessPayments = paymentRepository.findByEmailAndPaymentStatusAndDateTimeAfter(
                    email, "Success", thirtyDaysAgo);

            if (!recentSuccessPayments.isEmpty()) {
                // Keep account active if there was a successful payment recently
                payment.setAccountStatus("active");
            } else {
                // Otherwise, mark the account as inactive
                payment.setAccountStatus("inactive");
            }
        }

        // Save the current payment
        paymentRepository.save(payment);

        return ResponseEntity.ok("Payment status updated to " + status);
    }
}
