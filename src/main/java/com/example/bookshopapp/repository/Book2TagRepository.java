package com.example.bookshopapp.repository;

import com.example.bookshopapp.data.book.links.Book2TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Book2TagRepository extends JpaRepository<Book2TagEntity, Integer> {
}
