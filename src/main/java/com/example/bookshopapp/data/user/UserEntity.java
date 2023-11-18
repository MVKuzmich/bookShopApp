package com.example.bookshopapp.data.user;

import com.example.bookshopapp.data.book.file.FileDownloadEntity;
import com.example.bookshopapp.data.book.links.Book2UserEntity;
import com.example.bookshopapp.data.book.review.BookReviewEntity;
import com.example.bookshopapp.data.book.review.BookReviewLikeEntity;
import com.example.bookshopapp.data.book.review.MessageEntity;
import com.example.bookshopapp.data.bookrate.BookRateEntity;
import com.example.bookshopapp.data.enums.ContactType;
import com.example.bookshopapp.data.payments.BalanceTransactionEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
@ToString(exclude = {"book2UserEntitySet", "fileDownloadEntitySet", "balanceTransactionEntitySet",
"userReviewSet", "messageSet", "userContactSet", "userLikeSet", "bookRates"})
@EqualsAndHashCode(of = "id")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String hash;

    @Column(name = "reg_time", columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime regTime;

    @Column(columnDefinition = "INT NOT NULL")
    private Integer balance;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Book2UserEntity> book2UserEntitySet = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<FileDownloadEntity> fileDownloadEntitySet = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BalanceTransactionEntity> balanceTransactionEntitySet;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BookReviewEntity> userReviewSet;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MessageEntity> messageSet;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserContactEntity> userContactSet = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BookReviewLikeEntity> userLikeSet;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<BookRateEntity> bookRates;

    public UserEntity(String hash, LocalDateTime regTime, int balance, String name) {
        this.hash = hash;
        this.regTime = regTime;
        this.balance = balance;
        this.name = name;
    }

    public String getEmail() {
        Optional<UserContactEntity> contactOptional = userContactSet.stream()
                .filter(contact -> contact.getType() == ContactType.EMAIL)
                .findFirst();
        return contactOptional.isPresent() ? contactOptional.get().getContact() : "EMAIL was not confirmed";
    }

    public String getPhone() {
        Optional<UserContactEntity> contactOptional = userContactSet.stream()
                .filter(contact -> contact.getType() == ContactType.PHONE)
                .findFirst();
        return contactOptional.isPresent() ? contactOptional.get().getContact() : "PHONE was not confirmed";
    }
}

