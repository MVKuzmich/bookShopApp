package com.example.bookshopapp.data.user;


import com.example.bookshopapp.data.enums.ContactType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_contact")
@Getter @Setter @NoArgsConstructor
public class UserContactEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

//    @Column(name = "current_contact_type", columnDefinition = "contact_type")
    @Column(name = "current_contact_type")
    @Enumerated(EnumType.STRING)
    private ContactType type;

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private short approved;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String code;

    @Column(name = "code_trials", columnDefinition = "INT")
    private int codeTrials;

    @Column(name = "code_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime codeTime;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String contact;

    public UserContactEntity(UserEntity user, ContactType type, short approved, String code, int codeTrials, LocalDateTime codeTime, String contact) {
        this.user = user;
        this.type = type;
        this.approved = approved;
        this.code = code;
        this.codeTrials = codeTrials;
        this.codeTime = codeTime;
        this.contact = contact;
    }
}
