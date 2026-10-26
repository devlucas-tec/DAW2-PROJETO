package br.edu.ifpb.es.daw.rest.advice;

import br.edu.ifpb.es.daw.exception.ConflitoException;
import br.edu.ifpb.es.daw.exception.EntidadeNaoEncontradaException;
import br.edu.ifpb.es.daw.exception.EstadoInvalidoException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    enum ErrorType {
        ERRO_INESPERADO, REQUISICAO_INVALIDA, ESTADO_INVALIDO, ERRO_DE_VALIDACAO,
        ENTIDADE_NAO_ENCONTRADA, NAO_AUTENTICADO, ACESSO_NEGADO, CONFLITO
    }

    @ExceptionHandler({BadCredentialsException.class, LockedException.class, DisabledException.class})
    public ProblemDetail handleCredenciaisInvalidas(AuthenticationException ex) {
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, ErrorType.NAO_AUTENTICADO,
                "E-mail ou senha inválidos, ou conta desativada.");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException ex) {
        return buildProblemDetail(HttpStatus.UNAUTHORIZED, ErrorType.NAO_AUTENTICADO,
                "Você precisa estar autenticado para acessar este recurso. " +
                "Faça login em /auth/login e envie o token no header: Authorization: Bearer <token>");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException ex) {
        String detalhe = ex.getMessage() != null && !ex.getMessage().startsWith("Access Denied")
                ? ex.getMessage()
                : "Você não tem permissão para realizar esta operação.";
        return buildProblemDetail(HttpStatus.FORBIDDEN, ErrorType.ACESSO_NEGADO, detalhe);
    }

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ProblemDetail handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex) {
        return buildProblemDetail(HttpStatus.NOT_FOUND, ErrorType.ENTIDADE_NAO_ENCONTRADA, ex.getMessage());
    }

    @ExceptionHandler(EstadoInvalidoException.class)
    public ProblemDetail handleEstadoInvalidoException(EstadoInvalidoException ex) {
        return buildProblemDetail(HttpStatus.UNPROCESSABLE_ENTITY, ErrorType.ESTADO_INVALIDO, ex.getMessage());
    }

    @ExceptionHandler(ConflitoException.class)
    public ProblemDetail handleConflitoException(ConflitoException ex) {
        return buildProblemDetail(HttpStatus.CONFLICT, ErrorType.CONFLITO, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return buildProblemDetail(HttpStatus.CONFLICT, ErrorType.CONFLITO,
                "A operação viola uma restrição do banco de dados: registro duplicado " +
                "ou ainda referenciado por outros registros.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildProblemDetail(HttpStatus.BAD_REQUEST, ErrorType.REQUISICAO_INVALIDA, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception ex) {
        logger.error("Erro inesperado", ex);
        return buildProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.ERRO_INESPERADO,
                "Ocorreu um erro inesperado no servidor.");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> erros = new LinkedHashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = error instanceof FieldError fieldError ? fieldError.getField() : error.getObjectName();
            erros.put(campo, error.getDefaultMessage());
        });
        ProblemDetail problemDetail = buildProblemDetail(HttpStatus.BAD_REQUEST, ErrorType.ERRO_DE_VALIDACAO,
                "Um ou mais campos são inválidos.");
        problemDetail.setProperty("erros", erros);
        return ResponseEntity.badRequest().body(problemDetail);
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, ErrorType type, String detalhe) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detalhe);
        problemDetail.setType(URI.create(type.name()));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }
}
