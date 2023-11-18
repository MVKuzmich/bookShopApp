package com.example.bookshopapp.data.book.review;


import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.data.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "book_review")
@Getter
@Setter
@NoArgsConstructor
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    @OneToMany(mappedBy = "bookReviewEntity", fetch = FetchType.LAZY)
    private Set<BookReviewLikeEntity> bookReviewLikeSet = new HashSet<>();

    public BookReviewEntity(Book book, UserEntity user, LocalDateTime time, String text) {
        this.book = book;
        this.user = user;
        this.time = time;
        this.text = text;
    }
}
