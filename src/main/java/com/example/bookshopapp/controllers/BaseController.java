package com.example.bookshopapp.controllers;

import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.data.user.UserEntity;
import com.example.bookshopapp.dto.SearchWordDto;
import com.example.bookshopapp.service.BookService;
import com.example.bookshopapp.service.UserService;
import com.example.bookshopapp.util.CookieHandleUtil;
import org.apache.catalina.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
public abstract class BaseController {

    protected BookService bookService;
    protected UserService userService;
    protected BaseController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @ModelAttribute("searchWordDto")
    public SearchWordDto searchWordDto() {
        return new SearchWordDto();
    }

    @ModelAttribute("searchResults")
    public List<Book> searchResults() {
        return new ArrayList<>();
    }

    @ModelAttribute("myBooksCount")
    public Integer getBoughtBooksCount(Principal principal) {
        return principal == null ? 0 : bookService.getCountOfBoughtBooks(userService.getCurrentUser());
    }

    @ModelAttribute("postponedBooksCount")
    public Integer getPostponedBooksCount(Principal principal, @CookieValue(name =
            "postponedContents", required = false) String postponedContents)  {
        UserEntity currentUser = null;
        if(principal != null) {
            currentUser = userService.getCurrentUser();
        }
        return bookService.getCountOfPostponedBooks(postponedContents, currentUser);
    }

    @ModelAttribute("cartBooksCount")
    public Integer getCartBooksCount(Principal principal, @CookieValue(name =
            "cartContents", required = false) String cartContents)  {
        UserEntity currentUser = null;
        if(principal != null) {
            currentUser = userService.getCurrentUser();
        }
        return bookService.getCountOfCartBooks(cartContents, currentUser);
    }
}
