package br.edu.ifpb.es.daw.rest.advice;

import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    enum ErrorType {
        ERRO_INESPERADO, REQUISICAO_INVALIDA, ESTADO_INVALIDO, ERRO_DE_VALIDACAO, ENTIDADE_NAO_ENCONTRADA
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex) {
        return buildProblemDetail(ex, HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.ERRO_INESPERADO);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildProblemDetail(ex, HttpStatus.BAD_REQUEST, ErrorType.REQUISICAO_INVALIDA);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex) {
        return buildProblemDetail(ex, HttpStatus.NOT_FOUND, ErrorType.ENTIDADE_NAO_ENCONTRADA);
    }

    @ExceptionHandler(EstadoInvalidoException.class)
    public ProblemDetail handleEstadoInvalidoException(EstadoInvalidoException ex) {
        return buildProblemDetail(ex, HttpStatus.BAD_REQUEST, ErrorType.ESTADO_INVALIDO);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity.ofNullable(handleMethodArgumentNotValidException(ex));
    }

    private ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ProblemDetail problemDetail = buildProblemDetail(ex, HttpStatus.BAD_REQUEST, ErrorType.ERRO_DE_VALIDACAO);
        problemDetail.setProperty("erros", errors);
        return problemDetail;
    }

    private ProblemDetail buildProblemDetail(Exception ex, HttpStatus status, ErrorType type) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getLocalizedMessage());
        problemDetail.setType(URI.create(type.name()));
        problemDetail.setProperty("trace", stackTraceToString(ex));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    private String stackTraceToString(Exception ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        return errors.toString();
    }
}