package com.example.bookshopapp.data.bookrate;

import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.data.user.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "book_rates")
@NoArgsConstructor
@Getter
@Setter
public class BookRateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    private Integer value;

    public BookRateEntity(Book book, UserEntity user, Integer value) {
        this.book = book;
        this.user = user;
        this.value = value;
    }
}
