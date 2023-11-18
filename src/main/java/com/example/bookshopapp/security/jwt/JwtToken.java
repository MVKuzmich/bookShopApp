package com.example.bookshopapp.security.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "jwt_tokens")
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_value")
    private String tokenValue;

    @Column(name = "expiration_time")
    private LocalDate expirationTime;

    public JwtToken(String tokenValue, LocalDate expirationTime) {
        this.tokenValue = tokenValue;
        this.expirationTime = expirationTime;
    }
}
