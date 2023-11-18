package com.example.bookshopapp.dto;


import java.time.LocalDateTime;

public interface ReviewDto {

    Integer getId();
    String getUserName();
    LocalDateTime getTime();
    String getReviewText();
    Integer getCountLikes();
    Integer getCountDislikes();
    Integer getRating();

}
