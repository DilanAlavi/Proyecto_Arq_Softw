package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LoginResponse {
    private String token;
    private LocalDateTime timestamp;
}
