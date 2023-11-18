package com.example.bookshopapp.controllers;

import com.example.bookshopapp.dto.BookDto;
import com.example.bookshopapp.service.BookService;
import com.example.bookshopapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.util.List;

@Controller
public class RecentController extends BaseController{

    protected RecentController(BookService bookService, UserService userService) {
        super(bookService, userService);
    }

    @ModelAttribute("recentBooks")
    public List<BookDto> recentBooks(){
        LocalDate now = LocalDate.now();
        return bookService.getRecentBooksBetweenDesc(now.minusMonths(1L), now, 0, 6).getContent();
    }


    @GetMapping("/recent")
    public String recentPage() {
        return "books/recent";
    }

}
