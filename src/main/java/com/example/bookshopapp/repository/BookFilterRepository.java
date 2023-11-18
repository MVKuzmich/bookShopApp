package com.example.bookshopapp.repository;

import com.example.bookshopapp.data.book.Book;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookFilterRepository {

    Page<Book> findBooksByFilter(Predicate predicate, Pageable pageable);
}
