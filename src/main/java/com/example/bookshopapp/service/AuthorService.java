package com.example.bookshopapp.service;

import com.example.bookshopapp.data.author.Author;
import com.example.bookshopapp.dto.*;
import com.example.bookshopapp.mapper.AuthorMapper;
import com.example.bookshopapp.mapper.BookMapper;
import com.example.bookshopapp.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final BookMapper bookMapper;
    private final BookService bookService;


    public Map<String, List<AuthorDto>> getAuthorsMap() {
        List<AuthorDto> authors = authorRepository.findAllAuthorDto();
        return authors.stream().collect(Collectors.groupingBy((AuthorDto a) -> a.getName().substring(0,1)));
    }

    public AuthorUnitDto getAuthorUnitDtoBySlug(String authorSlug, Integer offset, Integer limit) {
        return authorRepository.findAuthorBySlug(authorSlug)
                .map(author -> authorMapper.toAuthorUnitDto(author, getBooksByAuthorSlug(author.getSlug(), offset, limit).getContent()))
                .orElseThrow();
    }

    public Page<BookDto> getBooksByAuthorSlug(String authorSlug, Integer offset, Integer limit) {
        return authorRepository.findBooksByAuthorSLug(authorSlug, PageRequest.of(offset, limit))
                .map(book -> bookMapper.toBookDto(book, bookService.getAuthors(book.getId())));
    }

}
