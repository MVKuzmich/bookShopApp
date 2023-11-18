package com.example.bookshopapp.repository;

import com.example.bookshopapp.data.book.review.BookReviewEntity;
import com.example.bookshopapp.dto.ReviewDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookReviewRepository extends JpaRepository<BookReviewEntity, Integer> {

    @Query(value = "select br.id as id, u.name as userName, br.time as time, br.text as reviewText, " +
            "(count(brl.review_id) filter (where brl.value = 1)) as countLikes, " +
            "(count(brl.review_id) filter (where brl.value = -1)) as countDisLikes, " +
            "(case " +
            "when (count(brl.review_id) filter (where brl.value = 1) = 0) and (count(brl.review_id) filter (where brl.value = -1) = 0) then 0 " +
            "when count(brl.review_id) filter (where brl.value = 1) - count(brl.review_id) filter (where brl.value = -1) < 5 then 1 " +
            "when count(brl.review_id) filter (where brl.value = 1) - count(brl.review_id) filter (where brl.value = -1) < 20 then 2 " +
            "when count(brl.review_id) filter (where brl.value = 1) - count(brl.review_id) filter (where brl.value = -1) < 100 then 3 " +
            "when count(brl.review_id) filter (where brl.value = 1) - count(brl.review_id) filter (where brl.value = -1) < 200 then 4 " +
            "else 5 " +
            "end) as rating " +
            "from book_review br " +
            "left join book_review_like brl on br.id = brl.review_id " +
            "left join users u on br.user_id = u.id " +
            "where br.book_id = :bookId " +
            "group by br.id, br.time, br.text, u.name " +
            "order by rating, countLikes desc ",
            nativeQuery = true)
    List<ReviewDto> findReviewsByBookId(Integer bookId);

}
