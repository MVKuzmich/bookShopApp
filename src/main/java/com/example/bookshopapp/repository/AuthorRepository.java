package com.example.bookshopapp.repository;

import com.example.bookshopapp.data.author.Author;
import com.example.bookshopapp.dto.AuthorDto;
import com.example.bookshopapp.dto.AuthorUnitModelDto;
import com.example.bookshopapp.dto.BookModelDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query("select new com.example.bookshopapp.dto.AuthorDto (a.slug, a.name) from Author a")
    List<AuthorDto> findAllAuthorDto();

    @Query("select new com.example.bookshopapp.dto.AuthorUnitModelDto " +
            "(a.id, a.photo, a.slug, a.name, a.description, count(b.id)) " +
            "from Author a " +
            "join a.book2AuthorEntitySet b2a " +
            "join b2a.book b " +
            "where a.slug = :slug " +
            "group by a.id")
    Optional<AuthorUnitModelDto> findAuthorBySlug(String slug);

    @Query("select new com.example.bookshopapp.dto.BookModelDto (b.id, b.slug, b.image, b.title, " +
            "b.price * 100, " +
            "b.isBestseller, " +
            "b.description, b.priceOld, b.priceOld * (1 - b.price)) " +
            "from Author a " +
            "join a.book2AuthorEntitySet b2a " +
            "join b2a.book b " +
            "where a.slug = :authorSlug " +
            "group by b.id, b.slug, b.image, b.title")
    Page<BookModelDto> findBooksByAuthorSLug(String authorSlug, Pageable pageable);

}
