package com.example.stripe_payment.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private LocalDateTime dateTime;
    private String paymentStatus;
    private String accountStatus;  // This field tracks active/inactive status

    // Constructors, Getters, and Setters

    public Payment() {}

    public Payment(String email, LocalDateTime dateTime, String paymentStatus, String accountStatus) {
        this.email = email;
        this.dateTime = dateTime;
        this.paymentStatus = paymentStatus;
        this.accountStatus = accountStatus;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
