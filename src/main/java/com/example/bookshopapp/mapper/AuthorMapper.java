package com.example.bookshopapp.mapper;

import com.example.bookshopapp.data.author.Author;
import com.example.bookshopapp.dto.AuthorUnitDto;
import com.example.bookshopapp.dto.AuthorUnitModelDto;
import com.example.bookshopapp.dto.BookDto;
import com.example.bookshopapp.dto.BookModelDto;
import com.example.bookshopapp.repository.AuthorRepository;
import com.example.bookshopapp.service.AuthorService;
import com.example.bookshopapp.service.BookService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    @Mapping(target = "description", source = "author", qualifiedByName = "descriptionToArray")
    AuthorUnitDto toAuthorUnitDto(AuthorUnitModelDto author, List<BookDto> books);

    @Named("descriptionToArray")
    default String[] descriptionToArray(AuthorUnitModelDto author) {
        String[] descrArray = author.getDescription().split("\\.");

        if(descrArray.length >= 3) {
            String visibleDescr = String.join(". ", Arrays.copyOfRange(descrArray, 0, 2)).concat(".");
            String hiddenDescr = String.join(". ", Arrays.copyOfRange(descrArray, 2, descrArray.length)).concat(".");
            return new String[]{visibleDescr, hiddenDescr};
        }
        return new String[]{author.getDescription()};
    }
}
