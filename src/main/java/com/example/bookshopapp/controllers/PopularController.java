package com.example.bookshopapp.controllers;

import com.example.bookshopapp.dto.BookDto;
import com.example.bookshopapp.service.BookService;
import com.example.bookshopapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class PopularController extends BaseController {

    protected PopularController(BookService bookService, UserService userService) {
        super(bookService, userService);
    }

    @ModelAttribute("popularBooks")
    public List<BookDto> popularBooks() {
        return bookService.getPopularBooks(0, 20).getContent();
    }


    @GetMapping("/popular")
    public String popularPage() {
        return "books/popular";
    }

}
