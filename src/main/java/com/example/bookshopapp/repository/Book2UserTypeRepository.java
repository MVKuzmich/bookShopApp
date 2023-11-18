package com.example.bookshopapp.repository;

import com.example.bookshopapp.data.book.links.Book2UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface Book2UserTypeRepository extends JpaRepository<Book2UserTypeEntity, Integer> {

    Book2UserTypeEntity findByCode(String code);

    @Query("select bookType.code " +
            "from Book2UserTypeEntity bookType " +
            "left join Book2UserEntity b2u on b2u.type.id = bookType.id " +
            "left join b2u.book b " +
            "where b.slug = :slug")
    Optional<String> findBookStatusByBookSlug(String slug);
}
