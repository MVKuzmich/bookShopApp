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
public class CartController extends BaseController {

    public static final String IS_CART_EMPTY = "isCartEmpty";
    public static final String CART_BOOKS_COOKIE_NAME = "cartContents";
    public static final String COOKIE_PATH = "/";

    private final Book2UserService book2UserService;

    protected CartController(BookService bookService, UserService userService, Book2UserService book2UserService) {
        super(bookService, userService);
        this.book2UserService = book2UserService;
    }

    @ModelAttribute(name = "bookCart")
    public List<Book> bookCart() {
        return new ArrayList<>();
    }

    @GetMapping("/cart")
    public String handleCartRequest(Principal principal,
                                    @CookieValue(value = "cartContents", required = false) String cartContents,
                                    Model model) {
        List<BookDto> booksInCart = new ArrayList<>();
        if (principal != null) {
            booksInCart = bookService.getBooksInCartByUser(userService.getCurrentUser());

        } else if (cartContents == null || cartContents.isEmpty()) {
            model.addAttribute(IS_CART_EMPTY, true);
        } else {
            model.addAttribute(IS_CART_EMPTY, false);
            cartContents = CookieHandleUtil.checkIfFirstOrEndSlashExist(cartContents);
            String[] cookieSlugs = cartContents.split("/");
            booksInCart = bookService.getBooksBySlugIn(cookieSlugs);
        }
        model.addAttribute("bookCart", booksInCart);
        model.addAttribute("totalPrice",
                booksInCart.isEmpty() ? 0 : booksInCart.stream()
                        .mapToInt(BookDto::getPrice)
                        .sum());
        model.addAttribute("totalDiscountPrice",
                booksInCart.isEmpty() ? 0 : booksInCart.stream()
                        .mapToInt(BookDto::getDiscountPrice)
                        .sum());

        return "cart";
    }

    @PostMapping("/changeBookStatus/cart/remove/{slug}")
    public String handleRemoveBookFromCartRequest(Principal principal,
                                                  @PathVariable("slug") String bookSlug,
                                                  @CookieValue(name = CART_BOOKS_COOKIE_NAME, required = false) String cartContents,
                                                  HttpServletResponse response,
                                                  Model model) {

        if (principal != null) {
            book2UserService.saveOrUpdateBook2User(userService.getCurrentUser(), "UNLINK", bookSlug);
        } else if (cartContents != null && !cartContents.isEmpty()) {
            Cookie cookie = CookieHandleUtil.removeElementFromCookieValue(bookSlug, cartContents, CART_BOOKS_COOKIE_NAME);
            cookie.setPath(COOKIE_PATH);
            response.addCookie(cookie);
            model.addAttribute(IS_CART_EMPTY, !cookie.getValue().isEmpty());
        }

        return "redirect:/books/cart";
    }

    @PostMapping("/changeBookStatus/{slug}")
    public String handleAddBookToCart(Principal principal,
                                      @PathVariable("slug") String bookSlug,
                                      @CookieValue(name = CART_BOOKS_COOKIE_NAME,
                                              required = false) String cartContents,
                                      HttpServletResponse response, Model model) {
        if(principal != null) {
            book2UserService.saveOrUpdateBook2User(userService.getCurrentUser(), "CART", bookSlug);
        } else {
            Cookie cookie = CookieHandleUtil.addElementToCookieValue(bookSlug, cartContents, CART_BOOKS_COOKIE_NAME);
            cookie.setPath(COOKIE_PATH);
            response.addCookie(cookie);
        }
        model.addAttribute(IS_CART_EMPTY, false);

        return "redirect:/books/" + bookSlug;
    }
}
