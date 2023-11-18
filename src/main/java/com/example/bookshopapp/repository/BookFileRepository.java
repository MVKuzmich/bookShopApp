package com.example.bookshopapp.repository;

import com.example.bookshopapp.data.book.BookFile;
import com.example.bookshopapp.dto.BookFileDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookFileRepository extends JpaRepository<BookFile, Integer> {

    BookFile findBookFileByHash(String hash);

    @Query("select new com.example.bookshopapp.dto.BookFileDto(bf.hash, ft.name) " +
            "from BookFile bf " +
            "join bf.fileType ft " +
            "where bf.book.id = :bookId")
    List<BookFileDto> findBookFileByBookId(Integer bookId);
}
