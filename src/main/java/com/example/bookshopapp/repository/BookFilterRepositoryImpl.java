package com.example.bookshopapp.repository;

import com.example.bookshopapp.data.book.Book;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static com.example.bookshopapp.data.author.QAuthor.author;
import static com.example.bookshopapp.data.book.QBook.book;
import static com.example.bookshopapp.data.book.links.QBook2AuthorEntity.book2AuthorEntity;
import static com.example.bookshopapp.data.book.links.QBook2TagEntity.book2TagEntity;
import static com.example.bookshopapp.data.genre.QGenreEntity.genreEntity;
import static com.example.bookshopapp.data.tag.QTagEntity.tagEntity;

@RequiredArgsConstructor
public class BookFilterRepositoryImpl implements BookFilterRepository {

    private final EntityManager entityManager;

    @Override
    public Page<Book> findBooksByFilter(Predicate predicate, Pageable pageable) {
        return new PageImpl<>(new JPAQuery<Book>(entityManager)
                .select(book)
                .from(book)
                .leftJoin(book.book2TagEntitySet, book2TagEntity)
                .leftJoin(book2TagEntity.tag, tagEntity)
                .leftJoin(book.genreSet, genreEntity)
                .leftJoin(book.book2AuthorEntitySet, book2AuthorEntity)
                .leftJoin(book2AuthorEntity.author, author)
                .where(predicate)
                .distinct()
                .orderBy(book.pubDate.desc())
                .fetch());
    }
}
