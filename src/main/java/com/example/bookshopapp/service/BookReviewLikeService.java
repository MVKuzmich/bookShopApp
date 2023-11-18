package com.example.bookshopapp.service;

import com.example.bookshopapp.data.book.review.BookReviewEntity;
import com.example.bookshopapp.data.book.review.BookReviewLikeEntity;
import com.example.bookshopapp.data.user.UserEntity;
import com.example.bookshopapp.repository.BookReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookReviewLikeService {

    private final BookReviewLikeRepository bookReviewLikeRepository;

    @Transactional
    public boolean handleBookReviewLikeOrDislike(BookReviewEntity bookReviewEntity, UserEntity user, short userValue) {
        Optional<BookReviewLikeEntity> likeDislikeOptional = bookReviewLikeRepository.findByUserAndBookReview(user, bookReviewEntity);
        if (likeDislikeOptional.isPresent()) {
            BookReviewLikeEntity likeDislike = likeDislikeOptional.get();
            if (likeDislike.getValue() != userValue) {
                likeDislike.setValue(userValue);
                likeDislike.setTime(LocalDateTime.now());
                bookReviewLikeRepository.saveAndFlush(likeDislike);
                return true;

            }
        } else {
            bookReviewLikeRepository.saveAndFlush(new BookReviewLikeEntity(
                    bookReviewEntity,
                    user,
                    LocalDateTime.now(),
                    userValue)
            );
            return true;
        }
        return false;
    }
}
