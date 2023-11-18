package com.example.bookshopapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AuthorPageBooksDto {

    private Integer count;
    private List<BookDto> books;

    public AuthorPageBooksDto(List<BookDto> books) {
        this.books = books;
        this.count = books.size();

    }
}
