package com.example.bookshopapp.controllers.integration;

import com.example.bookshopapp.IntegrationTestBase;
import com.example.bookshopapp.dto.UserContactConfirmationPayload;
import com.example.bookshopapp.dto.UserRegistrationForm;
import com.example.bookshopapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectWriter;

import static com.example.bookshopapp.dto.UserRegistrationForm.Fields.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class UserControllerTest extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final UserService userService;

    @Test
    void checkUserRegistrationMethod() throws Exception {
        mockMvc
                .perform(post("/register")
                        .param(name, "test")
                        .param(email, "test@gmail.com")
                        .param(phone, "+3757777777")
                        .param(password, "123456789")
                ).andExpectAll(
                        status().is2xxSuccessful(),
                        model().attributeExists("registrationOk"),
                        view().name("signin")
                );
    }

    @Test
    void checkUserLoginMethodIfUserIsRegistered() throws Exception {
        userService.registerNewUser(UserRegistrationForm.builder()
                .name("bob")
                .email("bob@gmail.com")
                .phone("+375447565635")
                .password("123456789")
                .build());
        UserContactConfirmationPayload userData = new UserContactConfirmationPayload("bob@gmail.com", "123456789");
        ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = mapper.writeValueAsString(userData);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void checkUserLoginMethodIfUserIsNOTRegistered() throws Exception {
        UserContactConfirmationPayload userData = new UserContactConfirmationPayload("bob@gmail.com", "123456789");
        ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = mapper.writeValueAsString(userData);
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameNotFoundException))
                .andExpect(result -> assertEquals("Username or password does not exist", result.getResolvedException().getMessage()));
    }

    @Test
    @WithMockUser
    void checkLogoutMethod() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(unauthenticated());
    }


}