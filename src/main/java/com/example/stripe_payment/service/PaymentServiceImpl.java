package com.example.stripe_payment.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.Stripe;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentIntent createPaymentIntent(Map<String, Object> params) throws StripeException {
        return PaymentIntent.create(params);
    }
}