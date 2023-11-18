package com.example.bookshopapp.controllers;

import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.dto.BookDto;
import com.example.bookshopapp.service.Book2UserService;
import com.example.bookshopapp.service.BookService;
import com.example.bookshopapp.service.UserService;
import com.example.bookshopapp.util.CookieHandleUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/books")
public class PostponedController extends BaseController {

    public static final String COOKIE_PATH = "/";
    public static final String POSTPONED_BOOKS_COOKIE_NAME = "postponedContents";
    public static final String IS_POSTPONED_EMPTY = "isPostponedEmpty";

    private final Book2UserService book2UserService;

    protected PostponedController(BookService bookService, UserService userService, Book2UserService book2UserService) {
        super(bookService, userService);
        this.book2UserService = book2UserService;
    }

    @ModelAttribute(name = "postponedBooks")
    public List<Book> bookCart() {
        return new ArrayList<>();
    }

    @GetMapping("/postponed")
    public String handlePostponed(Principal principal,
                                  @CookieValue(value = "postponedContents", required = false) String postponedContents,
                                  Model model) {
        List<BookDto> postponedBooks = new ArrayList<>();
        if (principal != null) {
            postponedBooks = bookService.getPostponedBooksByUser(userService.getCurrentUser());
            model.addAttribute(IS_POSTPONED_EMPTY, postponedBooks.isEmpty());
        } else if (postponedContents == null || postponedContents.isEmpty()) {
            model.addAttribute(IS_POSTPONED_EMPTY, true);
        } else {
            model.addAttribute(IS_POSTPONED_EMPTY, false);
            postponedContents = CookieHandleUtil.checkIfFirstOrEndSlashExist(postponedContents);
            String[] cookieSlugs = postponedContents.split("/");
            postponedBooks = bookService.getBooksBySlugIn(cookieSlugs);
        }
        model.addAttribute("postponedBooks", postponedBooks);
        model.addAttribute("totalPrice",
                postponedBooks.isEmpty() ? 0 : postponedBooks.stream()
                        .mapToInt(BookDto::getPrice)
                        .sum());
        model.addAttribute("totalDiscountPrice",
                postponedBooks.isEmpty() ? 0 : postponedBooks.stream()
                        .mapToInt(BookDto::getDiscountPrice)
                        .sum());

        return "postponed";
    }

    @PostMapping("/changeBookStatus/postponed/remove/{slug}")
    public String handleRemoveBookFromPostponed(Principal principal,
                                                @PathVariable("slug") String bookSlug,
                                                @CookieValue(name = "postponedContents", required = false) String postponedContents,
                                                HttpServletResponse response,
                                                Model model) {
        if (principal != null) {
            book2UserService.saveOrUpdateBook2User(userService.getCurrentUser(), "UNLINK", bookSlug);
        } else if (postponedContents != null && !postponedContents.isEmpty()) {
            Cookie cookie = CookieHandleUtil.removeElementFromCookieValue(bookSlug, postponedContents, POSTPONED_BOOKS_COOKIE_NAME);
            cookie.setPath(COOKIE_PATH);
            response.addCookie(cookie);
            model.addAttribute(IS_POSTPONED_EMPTY, !cookie.getValue().isEmpty());
        }

        return "redirect:/books/postponed";
    }


    @PostMapping("/changeBookStatus/postponed/{slug}")
    public String handleAddBookToPostponed(Principal principal,
                                           @PathVariable("slug") String bookSlug,
                                           @CookieValue(name = "postponedContents",
                                                   required = false) String postponedContents,
                                           HttpServletResponse response,
                                           Model model) {
        if(principal != null) {
            book2UserService.saveOrUpdateBook2User(userService.getCurrentUser(), "KEPT", bookSlug);
        } else {
            Cookie cookie = CookieHandleUtil.addElementToCookieValue(bookSlug, postponedContents, POSTPONED_BOOKS_COOKIE_NAME);
            cookie.setPath(COOKIE_PATH); //для доступности cookie на всех эндпоинтах books/
            response.addCookie(cookie);
        }

        model.addAttribute(IS_POSTPONED_EMPTY, false);
        return "redirect:/books/" + bookSlug;
    }

}
