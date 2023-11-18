package com.example.bookshopapp.dto;

import lombok.Value;

import java.util.List;

@Value
public class BookUnitDto {
    Integer id;
    String slug;
    String image;
    List<AuthorDto> authors;
    String title;
    Integer discount;
    Boolean isBestseller;
    Integer rating;
    String description;
    String status;
    Integer price;
    Integer discountPrice;
    List<TagDto> tags;
    List<ReviewDto> reviews;
    List<BookFileDto> bookFiles;


}
