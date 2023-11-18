package com.example.bookshopapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FAQController {

    @GetMapping("/faq")
    public String faqPage() {
        return "faq";
    }
}
