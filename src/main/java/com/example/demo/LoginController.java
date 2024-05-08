package com.example.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import com.networknt.schema.SpecVersion.VersionFlag;
import java.util.Set;
import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.http.MediaType;





@RestController
public class LoginController {

    private JsonSchema loadSchema(String path) {
        try (InputStream inputStream = new ClassPathResource(path).getInputStream()) {
            JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(VersionFlag.V7);
            return schemaFactory.getSchema(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Error loading JSON schema", e);
        }
    }
    

    @PostMapping("/login2")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Validate login request
            String jsonRequest = mapper.writeValueAsString(loginRequest);
            JsonSchema schemaRequest = loadSchema("schemas/loginRequest.json");
            JsonNode jsonNodeRequest = mapper.readTree(jsonRequest);
            Set<ValidationMessage> errorsRequest = schemaRequest.validate(jsonNodeRequest);

            if (!errorsRequest.isEmpty()) {
                String errorsCombined = errorsRequest.stream()
                                                     .map(ValidationMessage::getMessage)
                                                     .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body("Request validation error: " + errorsCombined);
            }

            // Create response and validate it
            LoginResponse response = new LoginResponse();
            response.setToken("generated-token-here");
            response.setTimestamp(LocalDateTime.now());

            JsonNode nodeResponse = mapper.valueToTree(response);
            JsonSchema schemaResponse = loadSchema("schemas/loginResponse.json");
            Set<ValidationMessage> errorsResponse = schemaResponse.validate(nodeResponse);

            if (!errorsResponse.isEmpty()) {
                String errorMessage = errorsResponse.stream()
                                                    .map(ValidationMessage::getMessage)
                                                    .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body("Response validation error: " + errorMessage);
            }

            return ResponseEntity.ok(response);

        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("JSON processing error: " + e.getMessage());
        }
    }
}
