package com.example.bookshopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class ReviewRateDto {

    @JsonProperty("reviewid")
    String reviewId;
    String value;
}
