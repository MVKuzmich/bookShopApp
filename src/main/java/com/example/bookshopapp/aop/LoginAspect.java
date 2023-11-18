package com.example.bookshopapp.aop;

import com.example.bookshopapp.dto.UserContactConfirmationPayload;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Aspect
@Slf4j
public class LoginAspect {

    @Pointcut("within(com.example.bookshopapp.controllers.UserController)")
    public void isUserControllerClass() {

    }
    @Pointcut("args(com.example.bookshopapp.dto.UserContactConfirmationPayload, ..)")
    public void isLoginMethod() {

    }

    @AfterReturning("isUserControllerClass() && isLoginMethod()")
    public void addLoggingAfterUserLogIn(JoinPoint joinPoint) {
        UserContactConfirmationPayload user = (UserContactConfirmationPayload) joinPoint.getArgs()[0];
        log.info("User {} has logged at {}", user.getContact(), LocalDateTime.now());
    }




}
