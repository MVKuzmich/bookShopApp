package com.example.bookshopapp.querydsl;

import lombok.Value;

import java.util.Set;

@Value
public class RecommendBooksFilter {

    Set<String> tags;
    Set<String> genres;
    Set<String> authors;

}
