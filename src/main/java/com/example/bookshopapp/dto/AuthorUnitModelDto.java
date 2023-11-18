package com.example.bookshopapp.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthorUnitModelDto {
    Integer id;
    String photo;
    String slug;
    String name;
    String description;
    Long countBooksByAuthor;
}
