package br.com.cadastroApi.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateKey(DuplicateKeyException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", 400);
        response.put("error", "Chave duplicada");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ClienteNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateKey(ClienteNaoEncontradoException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", 400);
        response.put("error", "Cliente não encontrado");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(CepInvalidoException.class)
    public ResponseEntity<Map<String, Object>> handleCepInvalido(CepInvalidoException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", 400);
        response.put("error", "CEP invalido");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(DataInvalidaException.class)
    public ResponseEntity<Map<String, Object>> handleDataInvalida(DataInvalidaException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", 400);
        response.put("error", "Data invalida");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseError(DataAccessException ex, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", 500);
        response.put("error", "Database Error");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", 400);
        response.put("error", "Argumento invalido");
        response.put("path", request.getRequestURI());

        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));

        response.put("messages", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", System.currentTimeMillis());
        response.put("status", 400);
        response.put("error", "Validaçao de parametro");
        response.put("message", ex.getMessage());
        response.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}


