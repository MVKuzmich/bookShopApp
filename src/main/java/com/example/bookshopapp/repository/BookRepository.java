package com.example.bookshopapp.repository;


import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.data.tag.TagEntity;
import com.example.bookshopapp.data.user.UserEntity;
import com.example.bookshopapp.dto.AuthorDto;
import com.example.bookshopapp.dto.BookDto;
import com.example.bookshopapp.dto.BookModelDto;
import com.example.bookshopapp.dto.BookUnitModelDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends
        JpaRepository<Book, Integer>,
        BookFilterRepository,
        QuerydslPredicateExecutor<Book> {

    Page<Book> findBooksByTitleContaining(String bookTitle, Pageable nextPage);

    @Query("select b from Book b where b.slug = :slug")
    Book findBySlug(String slug);

    /*
    SPECIAL METHODS FOR REST API
     */

    @Query(nativeQuery = true,
            value = "select * from books b " +
                    "join book2author b2a on b.id = b2a.book_id " +
                    "join authors a on b2a.author_id = a.id " +
                    "where a.name like '%:authorName%'")
    List<Book> findBooksByAuthorNameContaining(String authorName);

    List<Book> findBooksByTitleContaining(String bookTitle);

    List<Book> findBooksByPriceOldBetween(Integer min, Integer max);

    List<Book> findBooksByPriceOldIs(Integer price);

    @Query("from Book where isBestseller = 1")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM books WHERE discount = (SELECT MAX(discount) FROM books)", nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();

    List<Book> findBooksBySlugIn(String[] cookieSlugs);

    Page<Book> findBooksBySlugIn(List<String> cookieSlugs, Pageable pageable);

    @Query("select new com.example.bookshopapp.dto.AuthorDto(a.slug, a.name) " +
            "from Book b " +
            "join b.book2AuthorEntitySet b2a " +
            "join b2a.author a " +
            "where b.id = :bookId " +
            "order by b2a.sortIndex")
    List<AuthorDto> findAuthorsByBookId(Integer bookId);

    @Query("select new com.example.bookshopapp.dto.BookModelDto(b.id, b.slug, b.image, b.title, " +
            "b.price * 100, " +
            "b.isBestseller, " +
            "b.description, b.priceOld, b.priceOld * (1 - b.price)) " +
            "from Book b")
    Page<BookModelDto> findAllRecommendedBooks(Pageable pageable);


    @Query("select new com.example.bookshopapp.dto.BookModelDto (b.id, b.slug, b.image, b.title, " +
            "b.price * 100, " +
            "b.isBestseller, " +
            "b.description, b.priceOld, b.priceOld * (1 - b.price)) " +
            "from Book b " +
            "where b.pubDate > :fromDate and b.pubDate < :endDate " +
            "order by b.pubDate desc")
    Page<BookModelDto> findAllRecentBooksBetween(LocalDate fromDate, LocalDate endDate, Pageable pageable);

    @Query("select new com.example.bookshopapp.dto.BookModelDto (b.id, b.slug, b.image, b.title, " +
            "b.price * 100, " +
            "b.isBestseller, " +
            "b.description, b.priceOld, b.priceOld * (1 - b.price)) " +
            "from Book b " +
            "order by b.pubDate desc")
    Page<BookModelDto> findAllRecentBooks(Pageable pageable);

    @Query(value = "select res.* " +
            "from (" +
            "select b.*, " +
            "(count(case when b2ut.code = 'PAID' then 1 else null end) + " +
            "count(case when b2ut.code = 'CART' then 1 else null end) * 0.7 + " +
            "count(case when b2ut.code = 'KEPT' then 1 else null end) * 0.4) " +
            "as popularity " +
            "from books b " +
            "join book2user as b2u on b.id = b2u.book_id " +
            "join book2user_type b2ut on b2u.type_id = b2ut.id " +
            "group by b.id, b.slug, b.image, b.title) as res " +
            "order by popularity desc, res.title",
            nativeQuery = true,
            countQuery = "select count(*) from (" +
                    "select b.id, b.is_bestseller, b.slug, b.title, b.image, " +
                    "b.description, b.price, b.discount, " +
                    "(count(case when b2ut.code = 'PAID' then 1 else null end) + " +
                    "count(case when b2ut.code = 'CART' then 1 else null end) * 0.7 + " +
                    "count(case when b2ut.code = 'KEPT' then 1 else null end) * 0.4) " +
                    "as popularity " +
                    "from books b " +
                    "join book2user as b2u on b.id = b2u.book_id " +
                    "join book2user_type b2ut on b2u.type_id = b2ut.id " +
                    "group by b.id, b.slug, b.image, b.title) as res")
    Page<Book> findPopularBooks(Pageable pageable);

    @Query("select b.id as id, b.slug as slug, b.image as image, b.title as title, " +
            "b.price * 100 as discount, " +
            "b.isBestseller as bestseller, " +
            "avg(br.value) as rating, " +
            "b.description as description, " +
            "b.priceOld as price, " +
                    "b.priceOld * (1 - b.price) as discountPrice " +
                    "from Book b " +
                    "left join b.bookRateList br " +
            "where b.slug = :bookSlug " +
                    "group by b.id, b.slug, b.image, b.title")
    Optional<BookUnitModelDto> findBookUnitBySlug(String bookSlug);


    @Query("select new com.example.bookshopapp.dto.BookModelDto " +
            "(b.id, b.slug, b.image, b.title, " +
            "b.price * 100, " +
            "b.isBestseller, " +
            "b.description, " +
            "b.priceOld, b.priceOld * (1 - b.price)) " +
            "from Book b " +
            "join b.book2TagEntitySet b2t " +
            "join b2t.tag t " +
            "where t = :tag " +
            "order by b.pubDate desc")
    Page<BookModelDto> findBooksByTag(TagEntity tag, Pageable pageable);


    @Query("select b from Book b " +
            "join b.book2UserEntitySet b2u " +
            "join b2u.user u " +
            "join b2u.type t " +
            "where t.code in ('KEPT', 'CART', 'PAID') and u = :user")
    List<Book> findBooksByUserChoice(UserEntity user);

    @Query("select b from Book b " +
            "left join b.bookRateList br " +
            "group by b.id " +
            "order by avg(br.value) desc, b.pubDate desc")
    Page<Book> findBooksWithHighRating(Pageable pageable);


    Book findBookById(Integer bookId);

    @Query("select count(b) from Book b " +
            "join b.book2UserEntitySet b2u " +
            "join b2u.user u " +
            "join b2u.type t " +
            "where t.code = 'PAID' and u = :user")
    Integer findCountOfBoughtBooks(UserEntity user);

    @Query("select count(b) from Book b " +
            "join b.book2UserEntitySet b2u " +
            "join b2u.user u " +
            "join b2u.type t " +
            "where t.code = 'KEPT' and u = :user")
    Integer findCountOfPostponedBooks(UserEntity user);

    @Query("select count(b) from Book b " +
            "join b.book2UserEntitySet b2u " +
            "join b2u.user u " +
            "join b2u.type t " +
            "where t.code = 'CART' and u = :user")
    Integer findCountOfCartBooks(UserEntity user);

    @Query("select b from Book b " +
            "join b.book2UserEntitySet b2u " +
            "join b2u.user u " +
            "join b2u.type t " +
            "where t.code = 'CART' and u = :user")
    List<Book> findBooksInCart(UserEntity user);

    @Query("select b from Book b " +
            "join b.book2UserEntitySet b2u " +
            "join b2u.user u " +
            "join b2u.type t " +
            "where t.code = 'KEPT' and u = :user")
    List<Book> findBooksPostponed(UserEntity user);


}


