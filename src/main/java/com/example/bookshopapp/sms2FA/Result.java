package com.example.bookshopapp.sms2FA;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Result {
    @JsonProperty("password_object_id")
    Integer passwordObjectId;
}
