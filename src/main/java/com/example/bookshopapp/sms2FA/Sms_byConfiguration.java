package com.example.bookshopapp.sms2FA;

import com.example.bookshopapp.sms2FA.external.SMS_BY;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Sms_byConfiguration {

    @Value("${sms.token}")
    private String sms_byToken;

    @Bean
    public SMS_BY smsByBean() {
        return new SMS_BY(sms_byToken);
    }
}
