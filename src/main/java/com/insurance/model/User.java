package com.insurance.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // Enum: ADMIN, CUSTOMER, AGENT

    private String phone;
    private String address;
    private String gender;

    private LocalDate dateOfBirth;

    private String profilePicture;

    private Boolean emailVerified = false;

    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    private LocalDateTime lastLoginAt;

    @PreUpdate  
    public void setLastUpdated() {
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void setCreatedDefaults() {
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
}

}
