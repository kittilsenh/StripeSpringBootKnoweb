package com.example.stripe_payment.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import java.util.Map;

public interface PaymentService {
    PaymentIntent createPaymentIntent(Map<String, Object> params) throws StripeException;
}