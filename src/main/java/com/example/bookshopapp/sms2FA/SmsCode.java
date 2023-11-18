package com.example.bookshopapp.sms2FA;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sms_keys")
@NoArgsConstructor
@Setter
@Getter
public class SmsCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codeValue;
    private LocalDateTime expireTime;

    public SmsCode(String codeValue, Integer expireIn) {
        this.codeValue = codeValue;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }
}