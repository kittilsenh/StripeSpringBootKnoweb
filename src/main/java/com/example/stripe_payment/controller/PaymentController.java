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

// Import for date and time
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${stripe.api.secret-key}")
    private String stripeApiSecretKey;

    @Autowired
    private PaymentRepository paymentRepository;


    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, Object>> createPaymentIntent(@RequestBody Map<String, Object> data) {
        Stripe.apiKey = stripeApiSecretKey;

        // Extract email from the request body
        String email = (String) data.get("email");

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(5000L) // Amount in cents ($50.00)
                .setCurrency("usd")
                .setReceiptEmail(email) // Optional: send a receipt to the customer's email
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("clientSecret", paymentIntent.getClientSecret());

            // Check the status of the payment
            String status = paymentIntent.getStatus();
            if ("succeeded".equals(status)) {
                status = "payment_successful";
            } else if ("requires_payment_method".equals(status)) {
                status = "requires_payment_method";
            } else if ("requires_action".equals(status)) {
                status = "requires_action"; // Handle other cases accordingly
            } else {
                status = "unknown";
            }

            // Save payment details to the database
            Payment payment = new Payment();
            payment.setEmail(email);
            payment.setDateTime(LocalDateTime.now());
            payment.setPaymentStatus(status); // Update with the appropriate status

            paymentRepository.save(payment);

            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            // Save failed payment attempt
            Payment payment = new Payment();
            payment.setEmail(email);
            payment.setDateTime(LocalDateTime.now());
            payment.setPaymentStatus("Failed");

            paymentRepository.save(payment);
            // If using PaymentService:
            // paymentService.savePayment(payment);

            Map<String, Object> errorData = new HashMap<>();
            errorData.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorData);
        }
    }
}
