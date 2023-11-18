package com.example.bookshopapp.controllers;

import com.example.bookshopapp.dto.UserContactConfirmationPayload;
import com.example.bookshopapp.dto.UserContactConfirmationResponse;
import com.example.bookshopapp.dto.UserRegistrationForm;
import com.example.bookshopapp.emailsender.EmailSenderService;
import com.example.bookshopapp.service.Book2UserService;
import com.example.bookshopapp.service.BookService;
import com.example.bookshopapp.service.UserService;
import com.example.bookshopapp.sms2FA.SmsCode;
import com.example.bookshopapp.sms2FA.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class UserController extends BaseController {
    private final SmsService smsService;
    private final EmailSenderService emailSenderService;
    private final Book2UserService book2UserService;

    protected UserController(BookService bookService,
                             UserService userService,
                             SmsService smsService,
                             EmailSenderService emailSenderService,
                             Book2UserService book2UserService) {
        super(bookService, userService);
        this.smsService = smsService;
        this.emailSenderService = emailSenderService;
        this.book2UserService = book2UserService;
    }

    @GetMapping("/signin")
    public String handleSignInPage() {

        return "signin";
    }

    @GetMapping("/signup")
    public String handleSignUpPage(Model model) {
        model.addAttribute("regForm", new UserRegistrationForm());
        return "signup";
    }

    //заглушка для работы кнопки подтверждения
    @PostMapping("/registerContactConfirmation")
    @ResponseBody
    public UserContactConfirmationResponse handleRegisterContactConfirmation(@RequestBody UserContactConfirmationPayload payload) {
        UserContactConfirmationResponse response = new UserContactConfirmationResponse();
        if (payload.getContact().contains("@")) {
            String code = emailSenderService.sendEmail(payload.getContact());
            smsService.saveSmsCode(new SmsCode(code, 120));
            response.setResult("true");

            return response; // for email
        } else {
            /*
             TODO: 28.10.2023
             need to check sms sending functionality - no possibility to use sms.by service

            Sms_byResponse sendSmsResponse = smsService.sendSmsMessage(payload.getContact());
            smsService.saveSmsCode(new SmsCode(smsService.formatCode(sendSmsResponse.getCode()), 60));
             */

            response.setResult("true");
            return response;
        }
    }

    @PostMapping("/loginContactConfirmation")
    @ResponseBody
    public UserContactConfirmationResponse handleLoginContactConfirmation(@RequestBody UserContactConfirmationPayload payload) {
        UserContactConfirmationResponse response = new UserContactConfirmationResponse();
        if (payload.getContact().contains("@")) {
            response.setResult("true");
            return response; // for email
        } else {
            response.setResult("true");
            return response;
        }
    }

    //заглушка для работы кнопки подтверждения
    @PostMapping("/approveContact")
    @ResponseBody
    public UserContactConfirmationResponse handleApproveContact(@RequestBody UserContactConfirmationPayload payload) {
        UserContactConfirmationResponse response = new UserContactConfirmationResponse();
        if (payload.getContact().contains("@")) {
            if (smsService.verifyCode(payload.getCode())) {
                response.setResult("true");
            } else {
                response.setResult("false");
            }
        } else {
            /*
            if sms service will be fixed ->

            if(smsService.verifyCode(payload.getCode())) {
                response.setResult("true");
                return response;
            }
            return new UserContactConfirmationResponse();
             */
            response.setResult("true");
        }
        return response;
    }

    //
    @PostMapping("/register")
    public String handleUserRegistration(UserRegistrationForm userRegistrationForm,
                                         Model model) {
        userService.registerNewUser(userRegistrationForm);
        model.addAttribute("registrationOk", true);

        return "signin";
    }

    private void deleteCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


    //метод при работе с JWT
    @PostMapping("/login")
    @ResponseBody
    public UserContactConfirmationResponse handleLogin(@RequestBody UserContactConfirmationPayload payload,
                                                       @CookieValue(value = "postponedContents", required = false) String postponedContents,
                                                       @CookieValue(value = "cartContents", required = false) String cartContents,
                                                       HttpServletResponse response) {
        UserContactConfirmationResponse loginResponse = userService.jwtLogin(payload);

        if (postponedContents != null) {
            book2UserService.saveOrUpdateBooksChosenViaCookie(postponedContents, userService.getCurrentUser(), "KEPT");
        }

        if (cartContents != null) {
            book2UserService.saveOrUpdateBooksChosenViaCookie(cartContents, userService.getCurrentUser(), "CART");
        }

        Cookie cookie = new Cookie("token", loginResponse.getResult());
        response.addCookie(cookie);
        deleteCookie(response, "postponedContents");
        deleteCookie(response, "cartContents");
        return loginResponse;
    }


    @GetMapping("/my")
    public String handleMy() {

        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("currentUser", userService.getCurrentUser());
        return "profile";
    }
}


