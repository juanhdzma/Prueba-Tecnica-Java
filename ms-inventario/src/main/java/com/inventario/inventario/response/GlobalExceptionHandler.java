package com.inventario.inventario.response;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(
            MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
        log.warn("❌ Error de validación: {}", errores);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("Error de formato en los campos", errores));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        log.warn("❌ Violación de restricción: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("Violación de restricción", error));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleRuntime(RuntimeException ex) {
        log.warn("❌ Error de negocio: {}", ex.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("Error de negocio", error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleGeneral(Exception ex) {
        log.error("❗ Error interno inesperado", ex);
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno del servidor");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>("Error inesperado", error));
    }
}