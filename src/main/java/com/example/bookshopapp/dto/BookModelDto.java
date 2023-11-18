package com.example.bookshopapp.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookModelDto {
    Integer id;
    String slug;
    String image;
    String title;
    Float discount;
    Float bestseller;
    String description;
    Integer price;
    Float discountPrice;

}
