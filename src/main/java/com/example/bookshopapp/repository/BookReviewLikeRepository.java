package com.example.bookshopapp.repository;

import com.example.bookshopapp.data.book.review.BookReviewEntity;
import com.example.bookshopapp.data.book.review.BookReviewLikeEntity;
import com.example.bookshopapp.data.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookReviewLikeRepository extends JpaRepository<BookReviewLikeEntity, Integer> {

    @Query("select brl from BookReviewLikeEntity brl where brl.user = :user and brl.bookReviewEntity = :bookReview")
    Optional<BookReviewLikeEntity> findByUserAndBookReview(UserEntity user, BookReviewEntity bookReview);
}
