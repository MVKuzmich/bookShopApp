package com.example.bookshopapp.querydsl;

import com.example.bookshopapp.data.book.QBook;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;

@Component
public class QuerydslPredicateBuilder {

    public Predicate buildQueryPredicate(RecommendBooksFilter filter) {
        //проходим по каждой книге собираем в сет тэги, авторы, жанры и т.п.
        return QPredicate.builder()
                .add(filter.getTags(), QBook.book.book2TagEntitySet.any().tag.description::in)
                .add(filter.getGenres(), QBook.book.genreSet.any().name::in)
                .add(filter.getAuthors(), QBook.book.book2AuthorEntitySet.any().author.name::in)
                .buildOr();
    }

}
