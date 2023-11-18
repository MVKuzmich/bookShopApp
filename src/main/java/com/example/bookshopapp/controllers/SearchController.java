package com.example.bookshopapp.controllers;

import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.dto.BookDto;
import com.example.bookshopapp.dto.BooksPageDto;
import com.example.bookshopapp.dto.SearchWordDto;
import com.example.bookshopapp.errors.EmptySearchException;
import com.example.bookshopapp.mapper.BookMapper;
import com.example.bookshopapp.service.BookService;
import com.example.bookshopapp.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SearchController extends BaseController {

    private final BookMapper bookMapper;

    public SearchController(BookService bookService, UserService userService, BookMapper bookMapper) {
        super(bookService, userService);
        this.bookMapper = bookMapper;
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto
                                           searchWordDto, Model model) throws EmptySearchException {
        if (searchWordDto == null) {
            throw new EmptySearchException("Search is impossible, enter any word...");
        } else {
            Page<BookDto> page = bookService.getPageOfSearchResultsBooks(searchWordDto.getExample(), 0, 5)
                    .map(book -> bookMapper.toBookDto(book, bookService.getAuthors(book.getId())));
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("searchResults", page.getContent());
            model.addAttribute("totalCount", page.getTotalElements());
            return "/search/index";
        }
    }

    @GetMapping(value = {"/search/page/{searchWord}"})
    @ResponseBody
    public BooksPageDto getNextSearchResults(@PathVariable(value = "searchWord", required = false) SearchWordDto
                                           searchWordDto,
                                              @RequestParam(value = "offset", required = false) Integer offset,
                                              @RequestParam(value = "limit", required = false) Integer limit) throws EmptySearchException {
        if (searchWordDto == null) {
            throw new EmptySearchException("Search is impossible, enter any word...");
        } else {
            return new BooksPageDto(bookService.getPageOfSearchResultsBooks(searchWordDto.getExample(), offset, limit)
                    .map(book -> bookMapper.toBookDto(book, bookService.getAuthors(book.getId())))
                    .getContent());
        }
    }
}
