package com.example.bookshopapp.data.book;

import com.example.bookshopapp.data.author.Author;
import com.example.bookshopapp.data.book.file.FileDownloadEntity;
import com.example.bookshopapp.data.book.links.Book2AuthorEntity;
import com.example.bookshopapp.data.book.links.Book2TagEntity;
import com.example.bookshopapp.data.book.links.Book2UserEntity;
import com.example.bookshopapp.data.book.review.BookReviewEntity;
import com.example.bookshopapp.data.bookrate.BookRateEntity;
import com.example.bookshopapp.data.genre.GenreEntity;
import com.example.bookshopapp.data.payments.BalanceTransactionEntity;
import com.example.bookshopapp.data.tag.TagEntity;
import com.example.bookshopapp.data.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "books")
@Getter @Setter @NoArgsConstructor
@EqualsAndHashCode(of = {"id", "slug"})
@ToString(exclude = {"book2AuthorEntitySet", "book2TagEntitySet", "genreSet", "bookFileList",
"book2UserEntitySet", "fileDownloadEntitySet", "transactionSet", "bookReviewSet", "bookRateList"})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pub_date", columnDefinition = "date")
    private LocalDate pubDate;

    @Column(name = "is_bestseller", columnDefinition = "smallint")
    private Float isBestseller;
    private String slug;
    private String title;
    private String image;
    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "price")
    @JsonProperty("price")
    private Integer priceOld;

    @Column(name = "discount", columnDefinition = "decimal")
    @JsonProperty("discount")
    private float price;

    @JsonProperty("author")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @OneToMany(mappedBy = "book")
    private Set<Book2AuthorEntity> book2AuthorEntitySet = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private Set<Book2TagEntity> book2TagEntitySet = new HashSet<>();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "book2genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name ="genre_id"))
    private Set<GenreEntity> genreSet;

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private List<BookFile> bookFileList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private Set<Book2UserEntity> book2UserEntitySet = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "book")
    private Set<FileDownloadEntity> fileDownloadEntitySet = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BalanceTransactionEntity> transactionSet;

    @JsonIgnore
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BookReviewEntity> bookReviewSet = new TreeSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<BookRateEntity> bookRateList = new TreeSet<>();

    @JsonProperty
    public Integer discountPrice(){
        Integer discountedPriceInt = priceOld - Math.toIntExact(Math.round(price * priceOld));
        return discountedPriceInt;
    }
}
