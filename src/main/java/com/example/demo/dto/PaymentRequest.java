package com.example.demo.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private String cardNumber;
    private String cvv;
    private String expirationDate;
}
