package com.example.stripe_payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.stripe_payment.model.Payment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByEmail(String email);

    // Add this method to find successful payments within the last 30 days
    List<Payment> findByEmailAndPaymentStatusAndDateTimeAfter(String email, String paymentStatus, LocalDateTime dateTime);
}
