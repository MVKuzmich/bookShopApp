package com.example.bookshopapp.sms2FA;

import com.example.bookshopapp.sms2FA.external.SMS_BY;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SmsService {
    /*
    phone - номер телефона в формате 375291234567
     */

    private final SMS_BY sms_by;
    private final SmsCodeRepository smsCodeRepository;

    public Sms_byResponse sendSmsMessage(String phone) {
        String jsonResponse = sms_by.sendSmsMessageWithCode(
                "Ваш пароль: %CODE%",
                String.valueOf(getPasswordMessageId()),
                formatPhone(phone),
                0);
        return new Gson().fromJson(jsonResponse, Sms_byResponse.class);
    }

    private String formatPhone(String phone) {
        return phone.replaceAll("[^0-9]", "");
    }

    public void saveSmsCode(SmsCode code) {
        if(smsCodeRepository.findByCodeValue(code.getCodeValue()).isEmpty()) {
            smsCodeRepository.save(code);
        }
    }

    public Boolean verifyCode(String codeValue) {
        Optional<SmsCode> smsCodeOptional = smsCodeRepository.findByCodeValue(codeValue);
        return smsCodeOptional.isPresent() && !isExpired(smsCodeOptional.get());
    }

    public Boolean isExpired(SmsCode code){
        return LocalDateTime.now().isAfter(code.getExpireTime());
    }

    private String getPasswordMessageId() {
        return sms_by.createPasswordObject(SMS_BY.PASS_TYPE_NUMBERS, 6);
    }

    public String formatCode(String code) {
        StringBuilder sb = new StringBuilder(code);
        sb.insert(3, " ");
        return sb.toString();
    }

    public String generateCode() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(9));
        }
        sb.insert(3, " ");
    return sb.toString();

    }



}
