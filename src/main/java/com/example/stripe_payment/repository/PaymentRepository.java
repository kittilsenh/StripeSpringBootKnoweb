package com.example.stripe_payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.stripe_payment.model.Payment;
import java.util.Optional;

// Extend JpaRepository with your Payment entity and the type of its primary key (Long, Integer, etc.)
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Add this method to search for a payment by email
    Optional<Payment> findByEmail(String email);
}
