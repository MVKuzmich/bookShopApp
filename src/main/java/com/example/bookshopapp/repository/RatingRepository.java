package com.example.bookshopapp.repository;

import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.data.bookrate.BookRateEntity;
import com.example.bookshopapp.data.user.UserEntity;
import com.example.bookshopapp.dto.RateRangeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RatingRepository extends JpaRepository<BookRateEntity, Integer> {

    @Query("select br.value as rateValue, count(br.value) as rateCount " +
            "from BookRateEntity br " +
            "join br.book b " +
            "where b.slug = :bookSlug " +
            "group by br.value")
    List<RateRangeDto> findRateRangeForBook(String bookSlug);

    @Query("select avg(br.value) " +
            "from BookRateEntity br " +
            "join br.book b " +
            "where b.slug = :bookSlug")
    Integer findBookRatingBySlug(String bookSlug);


    @Query("select bre from BookRateEntity bre where bre.book = :book and bre.user = :user")
    Optional<BookRateEntity> findBookRateByUser(Book book, UserEntity user);
}
