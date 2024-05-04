package com.example.demo;

import io.sentry.Sentry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class ErrorController {

    @GetMapping("/trigger-error")
    public void triggerError() {
        try {
            // Lanza intencionalmente una excepción para probar la integración con Sentry
            throw new Exception("This is a test.");
        } catch (Exception e) {
            // Captura y envía la excepción a Sentry
            Sentry.captureException(e);
        }
    }
}
