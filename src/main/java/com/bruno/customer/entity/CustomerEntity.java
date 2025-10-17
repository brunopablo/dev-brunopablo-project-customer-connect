package com.bruno.customer.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

// Nome Completo
// CPF
// Email
// Telefone Celular
// Data de registro e atualização do cliente no sistema para fins de auditoria

@Entity
@Table(name = "tb_customers", uniqueConstraints = @UniqueConstraint(columnNames = {
        "customerId",
        "customerCpf",
        "customerEmail" }))
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    private String customerName;

    private Long customerCpf;

    private String customerEmail;

    private String customerPhone;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public CustomerEntity() {
    }

    public CustomerEntity(Long customerId, String customerName, Long customerCpf, String customerEmail,
            String customerPhone, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerCpf = customerCpf;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getCustomerCpf() {
        return customerCpf;
    }

    public void setCustomerCpf(Long customerCpf) {
        this.customerCpf = customerCpf;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
