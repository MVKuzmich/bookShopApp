package com.example.bookshopapp.service.integration;

import com.example.bookshopapp.IntegrationTestBase;
import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.dto.BookDto;
import com.example.bookshopapp.dto.BookUnitDto;
import com.example.bookshopapp.service.BookService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class BookServiceIT extends IntegrationTestBase {

    private final BookService bookService;

    @Test
    void checkSearchBooksMethod() {

        Page<Book> searchBooks = bookService.getPageOfSearchResultsBooks("Kronos", 0, 5);
        assertThat(searchBooks.getContent()).isNotEmpty();
        searchBooks.getContent().forEach(
                book -> assertTrue(book.getTitle().contains("Kronos"))
        );
    }

    @Test
    void checkGetBookBySlugMethod() {
        Book book = bookService.getBookBySlug("book-452-npg");
        assertThat(book).isNotNull();
        assertThat(book.getSlug()).isEqualTo("book-452-npg");
    }

    @Test
    void checkGetBestsellersMethod() {
        List<Book> bestsellers = bookService.getBestsellers();
        assertThat(bestsellers).isNotEmpty();
        bestsellers.forEach(book -> assertThat(book.getIsBestseller()).isEqualTo(1F));
    }

    @Test
    void checkGetBooksBySlugIn() {
        String[] slugs = {"book-452-npg", "book-978-den"};
        List<BookDto> books = bookService.getBooksBySlugIn(slugs);
        assertThat(books).isNotNull();
        assertThat(books).isNotEmpty();
        books.stream()
                .map(BookDto::getSlug)
                .forEach(slug -> assertTrue(Arrays.asList(slugs).contains(slug)));
    }

    @Test
    void getBookUnitDtoByBookSlug() {
        BookUnitDto bookUnit = bookService.getBookUnitDtoByBookSlug("book-452-npg");

        assertThat(bookUnit.getSlug()).isEqualTo("book-452-npg");
        assertThat(bookUnit.getDescription()).isEqualTo("Curabitur in libero ut massa volutpat convallis.");
        assertThat(bookUnit.getTitle()).isEqualTo("Kronos (a.k.a. Captain Kronos: Vampire Hunter)");
        assertThat(bookUnit.getAuthors()).isNotEmpty();
        assertThat(bookUnit.getTags()).isNotEmpty();
        assertThat(bookUnit.getReviews()).isNotEmpty();
    }
}