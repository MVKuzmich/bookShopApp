package com.example.bookshopapp.dto;

import com.example.bookshopapp.data.book.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class BooksPageDto {

    private Integer count;
    private List<BookDto> books;

    public BooksPageDto(List<BookDto> books) {
        this.books = books;
        this.count = books.size();

    }
}
