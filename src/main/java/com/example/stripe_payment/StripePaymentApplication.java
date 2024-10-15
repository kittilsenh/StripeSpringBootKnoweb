package com.example.stripe_payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Add this annotation
public class StripePaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(StripePaymentApplication.class, args);
	}
}
