package com.example.bookshopapp.controllers.integration;

import com.example.bookshopapp.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
@AutoConfigureMockMvc
class CartControllerTest extends IntegrationTestBase {

    private final MockMvc mockMvc;

    @Test
    void checkAddBookIntoCartUsingCookie() throws Exception {
        String slug = "book-452-npg";
        mockMvc.perform(post("/books/changeBookStatus/{slug}", slug))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getCookie("cartContents").getValue().contains(slug))
                .andExpect(redirectedUrlPattern("/books/*"));
    }

    @Test
    void checkRemoveBookFromCartUsingCookie() throws Exception {
        Cookie cartContents = new Cookie("cartContents", "book-452-npg/book-451-abc/");
        String slugForRemove = "book-452-npg";
        mockMvc.perform(post("/books/changeBookStatus/cart/remove/{slug}", slugForRemove)
                        .cookie(cartContents))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getCookie("cartContents").getValue().equals("/book-451-abc/"))
                .andExpect(redirectedUrl("/books/cart"));
    }


}