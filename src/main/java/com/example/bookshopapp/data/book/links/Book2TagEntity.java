package com.example.bookshopapp.data.book.links;

import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.data.tag.TagEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "book2tag")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Book2TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    private TagEntity tag;

}
