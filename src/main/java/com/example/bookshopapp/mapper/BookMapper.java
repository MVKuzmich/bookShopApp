package com.example.bookshopapp.mapper;

import com.example.bookshopapp.data.book.Book;
import com.example.bookshopapp.dto.*;
import com.example.bookshopapp.service.Book2UserService;
import com.example.bookshopapp.service.RatingService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class BookMapper {

    @Autowired
    protected RatingService ratingService;
    @Autowired
    protected Book2UserService book2UserService;

    @Mapping(target = "authors", source = "authors", qualifiedByName = "authorsToString")
    @Mapping(target = "status", constant = "none")
    @Mapping(target = "isBestseller", source = "book.bestseller", qualifiedByName = "integerToBoolean")
    public abstract BookDto toBookDto(BookModelDto book, List<AuthorDto> authors);


    @Mapping(target = "isBestseller", source = "book.isBestseller", qualifiedByName = "integerToBoolean")
    @Mapping(target = "authors", source = "authors", qualifiedByName = "authorsToString")
    @Mapping(target = "status", constant = "none")
    @Mapping(target = "discount", source = "book", qualifiedByName = "priceToDiscount")
    @Mapping(target = "price", source = "book.priceOld")
    @Mapping(target = "discountPrice", source = "book", qualifiedByName = "getDiscountPrice")
    @Mapping(target = "rating", expression = "java(ratingService.getBookRating(book.getSlug()))")
    public abstract BookDto toBookDto(Book book, List<AuthorDto> authors);


    @Mapping(target = "isBestseller", source = "book.bestseller", qualifiedByName = "integerToBoolean")
    @Mapping(target = "status", expression = "java(book2UserService.getBookStatusBySlug(book.getSlug()))")
    @Mapping(target = "rating", expression = "java(ratingService.getBookRating(book.getSlug()))")
    public abstract BookUnitDto toBookUnitDto(BookUnitModelDto book,
                              List<AuthorDto> authors,
                              List<ReviewDto> reviews,
                              List<TagDto> tags,
                              List<BookFileDto> bookFiles);

    @Named("authorsToString")
    public String authorsToString(List<AuthorDto> authors) {
        return authors.size() == 1 ? authors.get(0).getName() : authors.get(0).getName().concat(" and others");
    }

    @Named("integerToBoolean")
    public boolean integerToBoolean(Integer bestseller) {
        return bestseller == 1;
    }

    @Named("priceToDiscount")
    public Integer priceToDiscount(Book book) {
        return (int) (book.getPrice() * 100);
    }

    @Named("getDiscountPrice")
    public Integer getDiscountPrice(Book book) {
        return (int) (book.getPriceOld() * (1 - book.getPrice()));
    }

}
