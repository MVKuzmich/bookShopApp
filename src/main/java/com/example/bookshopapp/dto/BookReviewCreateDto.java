package com.example.bookshopapp.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class BookReviewCreateDto {

    @JsonProperty(value = "bookId")
    String bookSlug;
    String text;

}
