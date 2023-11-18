package com.example.bookshopapp.sms2FA;

import lombok.Value;

@Value
public class Sms_byResponse {

    String status;
    String parts;
    String len;
    String sms_id;
    String code;

}
