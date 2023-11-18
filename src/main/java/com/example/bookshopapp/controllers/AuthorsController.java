package com.example.bookshopapp.controllers;


import com.example.bookshopapp.dto.AuthorPageBooksDto;
import com.example.bookshopapp.service.AuthorService;

import com.example.bookshopapp.service.BookService;
import com.example.bookshopapp.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class AuthorsController extends BaseController {

    private final AuthorService authorService;

    public AuthorsController(BookService bookService, UserService userService, AuthorService authorService) {
        super(bookService, userService);
        this.authorService = authorService;
    }

    @GetMapping("/authors")
    public String authorsPage(Model model) {
        model.addAttribute("authorsMap", authorService.getAuthorsMap());
        return "/authors/index";
    }

    @GetMapping("/authors/{slug}")
    public String personalAuthorPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("author", authorService.getAuthorUnitDtoBySlug(slug, 0, 5));

        return "authors/slug";
    }

    @GetMapping("/books/author/{slug}")
    public String authorBooksPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("author", authorService.getAuthorUnitDtoBySlug(slug, 0, 10));

        return "books/author";
    }

    @GetMapping(value = "/books/author/{slug}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public AuthorPageBooksDto getAllBooksByAuthor(@PathVariable("slug") String slug,
                                                  @RequestParam("offset") Integer offset,
                                                  @RequestParam("limit") Integer limit) {
        return new AuthorPageBooksDto(authorService.getBooksByAuthorSlug(slug, offset, limit).getContent());
    }

}
