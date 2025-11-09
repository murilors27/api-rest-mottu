package com.mottu.rastreamento.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(basePackages = "com.mottu.rastreamento.controller")
public class WebExceptionHandler {

    private ModelAndView page(HttpStatus status, String title, String message) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("status", status.value());
        mv.addObject("error", title);
        mv.addObject("message", message);
        mv.setStatus(status); // <-- garante status HTTP correto no web
        return mv;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ModelAndView handleNotFound(EntityNotFoundException ex) {
        return page(HttpStatus.NOT_FOUND, "Recurso não encontrado", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleValidation(MethodArgumentNotValidException ex) {
        return page(HttpStatus.BAD_REQUEST, "Erro de validação", "Campos inválidos.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView handleDataIntegrity(DataIntegrityViolationException ex) {
        String msg = String.valueOf(ex.getMessage());
        String message;
        if (msg.contains("motos_identificador_uwb_key")) {
            message = "Já existe uma moto cadastrada com este Identificador UWB.";
        } else if (msg.contains("fk_alocacao_moto")) {
            message = "Não é possível excluir esta moto: há vínculo de alocação (histórico).";
        } else if (msg.contains("fk_moto_sensor")) {
            message = "Não é possível excluir este sensor: está vinculado a uma moto.";
        } else {
            message = "Operação não permitida por restrição de integridade.";
        }
        return page(HttpStatus.CONFLICT, "Violação de integridade de dados", message);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneric(Exception ex) {
        return page(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor",
                "Ocorreu um erro inesperado. Tente novamente.");
    }
}
