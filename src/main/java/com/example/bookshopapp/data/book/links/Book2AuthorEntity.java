package com.example.bookshopapp.data.book.links;

import com.example.bookshopapp.data.author.Author;
import com.example.bookshopapp.data.book.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "book2author")
@Getter @Setter
@NoArgsConstructor
public class Book2AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="book_id", columnDefinition = "INT NOT NULL")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author_id", columnDefinition = "INT NOT NULL")
    private Author author;

    @Column(name="sort_index", columnDefinition = "INT NOT NULL  DEFAULT 0")
    private Integer sortIndex;


}
