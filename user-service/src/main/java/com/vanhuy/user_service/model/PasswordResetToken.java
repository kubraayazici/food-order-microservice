package com.vanhuy.user_service.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "password_reset_token")
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private boolean used = false;

    public PasswordResetToken(User user) {
        this.token = UUID.randomUUID().toString();
        this.user = user ;
        this.expiryDate = Instant.now().plusSeconds(24 * 60 * 60); // 24 hours
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }
}
