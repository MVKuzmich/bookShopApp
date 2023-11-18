package com.example.bookshopapp.sms2FA;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsCodeRepository extends JpaRepository<SmsCode,Long> {

    Optional<SmsCode> findByCodeValue(String codeValue);
}
