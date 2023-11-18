package com.example.bookshopapp.dto;

import lombok.*;
import org.mapstruct.Named;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class BookRateCreateDto {

    private String bookId;
    private String value;


}
