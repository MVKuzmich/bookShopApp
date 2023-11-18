package com.example.bookshopapp.security.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JwtRepository extends JpaRepository<JwtToken, Long> {
    @Query("select t from JwtToken t where t.tokenValue = :tokenValue")
    Optional<JwtToken> findTokenByTokenValue(String tokenValue);
}
