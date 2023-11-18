package com.example.bookshopapp.data.tag;

import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.data.book.links.Book2TagEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"book2TagEntitySet"})
@ToString(exclude = "book2TagEntitySet")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;


    @OneToMany(mappedBy = "tag")
    private Set<Book2TagEntity> book2TagEntitySet = new HashSet<>();

}
