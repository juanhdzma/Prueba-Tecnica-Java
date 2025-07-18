package com.productos.productos.response;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.productos.productos.config.CachedBodyHttpServletRequest;

import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        String requestBody = extractRequestBody(request);
        log.warn("❌ Error de validación: {}\n📦 Datos recibidos: {}", errores, requestBody);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("Error de formato en los campos", errores));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        Map<String, String> errores = new HashMap<>();
        String campo = "campo";

        if (ex.getCause() instanceof com.fasterxml.jackson.databind.exc.MismatchedInputException mismatched &&
                !mismatched.getPath().isEmpty()) {
            campo = mismatched.getPath().get(0).getFieldName();
        }

        errores.put(campo, "Formato inválido");

        String requestBody = extractRequestBody(request);
        log.warn("❌ Error de deserialización en el campo '{}': {}\n📦 Datos recibidos: {}",
                campo, ex.getMessage(), requestBody);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("Error de formato en los campos", errores));
    }

    private String extractRequestBody(HttpServletRequest request) {
        if (request instanceof CachedBodyHttpServletRequest cached) {
            return cached.getCachedBodyAsString();
        }
        return "No se pudo leer el cuerpo";
    }
}