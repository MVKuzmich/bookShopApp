package com.example.bookshopapp.controllers;

import com.example.bookshopapp.data.user.UserEntity;
import com.example.bookshopapp.dto.BooksPageDto;
import com.example.bookshopapp.service.BookService;
import com.example.bookshopapp.service.TagService;
import com.example.bookshopapp.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.time.LocalDate;

@Controller
public class MainPageController extends BaseController {

    private final TagService tagService;

    protected MainPageController(BookService bookService, TagService tagService, UserService userService) {
        super(bookService, userService);
        this.tagService = tagService;
    }

    @GetMapping("/")
    public String mainPage(Model model,
                           Principal principal,
                           @CookieValue(value = "cartContents", required = false) String cartContents,
                           @CookieValue(value = "postponedContents", required = false) String postponedContents) {
        UserEntity currentUser = null;
        if(principal != null) {
            currentUser = userService.getCurrentUser();
        }
        model.addAttribute("recommendedBooks", bookService.getRecommendedBooks(currentUser, cartContents, postponedContents, 0, 6).getContent());
        model.addAttribute("recentBooks", bookService.getRecentBooks(0, 6).getContent());
        model.addAttribute("popularBooks", bookService.getPopularBooks(0, 6).getContent());
        model.addAttribute("tags", tagService.getAllTagsDto());

        return "index";
    }

    @GetMapping("/books/recommended")
    @ResponseBody
    public BooksPageDto getRecommendedBooksPage(
            Principal principal,
            @CookieValue(value = "cartContents", required = false) String cartContents,
            @CookieValue(value = "postponedContents", required = false) String postponedContents,
            @RequestParam("offset") Integer offset,
            @RequestParam("limit") Integer limit) {
        UserEntity currentUser = null;
        if(principal != null) {
            currentUser = userService.getCurrentUser();
        }
        return new BooksPageDto(bookService.getRecommendedBooks(currentUser, cartContents, postponedContents, offset,
                        limit).getContent());
    }

    @GetMapping(value = "/books/recent")
    @ResponseBody
    public BooksPageDto getRecentBooksBetween(@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate from,
                                              @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate to,
                                              @RequestParam(value = "offset", required = false) Integer offset,
                                              @RequestParam(value = "limit", required = false) Integer limit) {

        return new BooksPageDto(bookService.getRecentBooksBetweenDesc(from, to, offset, limit).getContent());
    }

    @GetMapping("/books/popular")
    @ResponseBody
    public BooksPageDto getPopularBooksPage(@RequestParam("offset") Integer offset, @RequestParam("limit") Integer
            limit) {
        return new BooksPageDto(bookService.getPopularBooks(offset, limit).getContent());
    }
}
