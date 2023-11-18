package com.example.bookshopapp.service;

import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.data.book.review.BookReviewEntity;
import com.example.bookshopapp.dto.ReviewDto;
import com.example.bookshopapp.repository.BookRepository;
import com.example.bookshopapp.repository.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final BookRepository bookRepository;

    @Transactional
    public void saveBookReview(BookReviewEntity bookReviewEntity) {
        bookReviewRepository.saveAndFlush(bookReviewEntity);
    }

    public BookReviewEntity getReviewById(Integer reviewId) {
        return bookReviewRepository.findById(reviewId).get();
    }

    public List<ReviewDto> getBookReviewList(Integer bookId) {
        return bookReviewRepository.findReviewsByBookId(bookId);
    }
}
