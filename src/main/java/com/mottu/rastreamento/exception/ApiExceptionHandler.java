package com.mottu.rastreamento.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.mottu.rastreamento.controller.api")
public class ApiExceptionHandler {

    private ResponseEntity<Map<String, Object>> body(HttpStatus status, String message) {
        Map<String, Object> b = new HashMap<>();
        b.put("timestamp", LocalDateTime.now());
        b.put("status", status.value());
        b.put("message", message);
        return ResponseEntity
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(b);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatus(ResponseStatusException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        String msg = ex.getReason() != null ? ex.getReason() : "Erro na requisição.";
        return body(status, msg);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        String raw = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : "";
        String msg;

        if (raw.contains("fk_alocacao_moto")) {
            msg = "Não é possível excluir esta moto, pois ela está vinculada a uma alocação.";
        }
        else if (raw.contains("fk_moto_sensor") || raw.contains("sensor_id") || raw.contains("restricao_fk")) {
            msg = "Não é possível excluir este sensor, pois ele está vinculado a uma moto.";
        }
        else if (raw.contains("motos_identificador_uwb_key")) {
            msg = "Já existe uma moto cadastrada com este Identificador UWB.";
        }
        else {
            msg = "Operação não permitida. Verifique vínculos e restrições antes de continuar.";
        }


        return body(HttpStatus.CONFLICT, msg);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(EntityNotFoundException ex) {
        return body(HttpStatus.NOT_FOUND, ex.getMessage() != null ? ex.getMessage() : "Recurso não encontrado.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return body(HttpStatus.BAD_REQUEST, "Tipo de dado inválido: " + ex.getName());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return body(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado.");
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Erro de validação.");

        return body(HttpStatus.BAD_REQUEST, mensagem);
    }

}
