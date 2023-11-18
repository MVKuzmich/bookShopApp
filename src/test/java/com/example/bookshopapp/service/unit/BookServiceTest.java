package com.example.bookshopapp.service.unit;

import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.data.user.UserEntity;
import com.example.bookshopapp.mapper.BookMapper;
import com.example.bookshopapp.querydsl.QuerydslPredicateBuilder;
import com.example.bookshopapp.repository.BookRepository;
import com.example.bookshopapp.service.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private QuerydslPredicateBuilder predicateBuilder;
    @InjectMocks
    @Spy
    private BookService bookService;


    @Test
    void getRecommendedBooksIfUserAuthenticated() {
        UserEntity user = new UserEntity(
                "hash",
                LocalDateTime.now(),
                0,
                "mick@gmail.com"
        );
        doReturn(Page.empty()).when(bookService)
                .getRecommendBooksByPredicate(anyList(), anyInt(), anyInt());
        when(bookRepository.findBooksByUserChoice(user))
                .thenReturn(List.of(new Book(), new Book()));

        bookService.getRecommendedBooks(user, null, null, 0, 5);

        verify(bookService).getRecommendBooksByPredicate(anyList(), anyInt(), anyInt());
        verify(bookRepository, times(0)).findBooksWithHighRating(any());
        verify(bookRepository, times(1)).findBooksByUserChoice(user);
    }

    @Test
    void getRecommendedBooksIfCookieHaveBookSlugsAndUserUnknown() {
        String postponedContents = "bookSlug";

        doReturn(Page.empty()).when(bookService)
                .getRecommendBooksByPredicate(anyList(), anyInt(), anyInt());
        when(bookRepository.findBooksBySlugIn(new String[]{postponedContents})).thenReturn(List.of(new Book(), new Book()));

        bookService.getRecommendedBooks(null, null, postponedContents, 0, 5);

        verify(bookService).getRecommendBooksByPredicate(anyList(), anyInt(), anyInt());
        verify(bookRepository, times(0)).findBooksByUserChoice(any(UserEntity.class));

    }

    @Test
    void getPopularBooks() {
        when(bookRepository.findPopularBooks(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(new Book(), new Book())));

        assertThat(bookService.getPopularBooks(0, 5).getContent()).hasSize(2);

        verify(bookRepository, times(1)).findPopularBooks(any());
    }

    @Test
    void getPageOfSearchResultsBooks() {
        Page<Book> books = new PageImpl<>(List.of(new Book(), new Book()));
        when(bookRepository.findBookByTitleContaining(anyString(), any(PageRequest.class))).thenReturn(books);

        Page<Book> actual = bookService.getPageOfSearchResultsBooks("doctor", 0, 5);

        assertThat(actual).hasSize(books.getSize());
        verify(bookRepository, times(1)).findBookByTitleContaining(anyString(), any(PageRequest.class));
    }

}
