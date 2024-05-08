package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    
    @NotNull
    final String message;
    public ErrorResponse(String message) {
        this.message = message;
    }
}