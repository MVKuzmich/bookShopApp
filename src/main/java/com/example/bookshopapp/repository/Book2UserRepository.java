package com.example.bookshopapp.repository;

import com.example.bookshopapp.data.book.links.Book2UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface Book2UserRepository extends JpaRepository<Book2UserEntity, Integer> {

    @Query("select b2u from Book2UserEntity b2u " +
            "left join b2u.book b " +
            "where b.slug = :slug")
    Optional<Book2UserEntity> findBook2UserEntityByBookSlug(String slug);
}
