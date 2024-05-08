package com.example.demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.PaymentResponse;
import com.example.demo.dto.ErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
public class PaymentController {

    @PostMapping("/payment")
    @Operation(summary = "Process a credit card payment", description = "Processes a payment using credit card details.")
    @ApiResponse(responseCode = "200", description = "Payment processed successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponse.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class)))
    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class)))
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest paymentRequest) {

        if (paymentRequest.getCardNumber() == null || paymentRequest.getCvv() == null || paymentRequest.getExpirationDate() == null) {
            ErrorDto errorDto = new ErrorDto("400", "Invalid payment request");
            return ResponseEntity.status(400).body(errorDto);
        }

        // Simulación de lógica de autorización
        if (!isAuthorized(paymentRequest)) {
            ErrorDto errorDto = new ErrorDto("401", "Unauthorized payment request");
            return ResponseEntity.status(401).body(errorDto);
        }
        try {
            PaymentResponse response = new PaymentResponse();
            response.setStatusCode(0);
            response.setMessage("Payment processed successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorDto errorDto = new ErrorDto("500", "Error processing payment");
            return ResponseEntity.status(500).body(errorDto);
        }
    }

    private boolean isAuthorized(PaymentRequest paymentRequest) {
        return true;
    }
}
