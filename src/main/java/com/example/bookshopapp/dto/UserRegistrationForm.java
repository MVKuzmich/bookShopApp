package com.example.bookshopapp.dto;

import lombok.*;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants
public class UserRegistrationForm {

    private String name;
    private String email;
    private String phone;
    private String password;

}
