package com.example.bookshopapp.emailsender;

import com.example.bookshopapp.sms2FA.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender javaMailSender;
    private final SmsService smsService;

    public String sendEmail(String toEmail) {
        SimpleMailMessage mailMessage = createMessage(toEmail);
        String code = smsService.generateCode();
        mailMessage.setText("Your code " + code);
        javaMailSender.send(mailMessage);

        return code;
    }

    public SimpleMailMessage createMessage(String toEmail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Complete Registration!");


        return mailMessage;
    }

}
