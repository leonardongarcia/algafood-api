package com.garcia.algafoodapi.api.exceptionhandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import com.garcia.algafoodapi.core.validation.ValidacaoException;
import com.garcia.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.garcia.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.garcia.algafoodapi.domain.exception.NegocioException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  public static final String MSG_DADOS_INVALIDOS =
      "Um ou mais campos est??o inv??lidos. Fa??a o preenchimento correto e tente novamente!";
  @Autowired private MessageSource messageSource;

  private static final String MSG_ERRO_GENERICA_USUARIO_FINAL =
      "Ocorreu um erro interno inesperado no sistema. Tente novamente e se o "
          + "problema persistir, entre em contato com o administrador do sistema.";

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
      HttpMediaTypeNotAcceptableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return ResponseEntity.status(status).headers(headers).build();
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    Throwable rootCause = ExceptionUtils.getRootCause(ex);

    if (rootCause instanceof InvalidFormatException) {
      return handleInvalidFormatException(
          (InvalidFormatException) rootCause, headers, status, request);
    } else if (rootCause instanceof PropertyBindingException) {
      return handlePropertyBindingException(
          (PropertyBindingException) rootCause, headers, status, request);
    }
    ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
    String detail = "O corpo da requisi????o est?? inv??lido. Verifique erro de sintaxe!";
    Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();

    return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
  }

  private ResponseEntity<Object> handleValidationInternal(
      Exception ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request,
      BindingResult bindingResult) {
    ProblemType problemType = ProblemType.DADOS_INVALIDOS;
    String detail =
        "Um ou mais campos est??o inv??lidos. Fa??a o preenchimento correto e tente novamente.";

    List<Problem.Object> problemObjects =
        bindingResult.getAllErrors().stream()
            .map(
                objectError -> {
                  String message =
                      messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                  String name = objectError.getObjectName();

                  if (objectError instanceof FieldError) {
                    name = ((FieldError) objectError).getField();
                  }

                  return Problem.Object.builder().name(name).userMessage(message).build();
                })
            .collect(Collectors.toList());

    Problem problem =
        createProblemBuilder(status, problemType, detail)
            .userMessage(detail)
            .objects(problemObjects)
            .build();

    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(
      BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    return handleValidationInternal(ex, headers, status, request, ex.getBindingResult());
  }

  private ResponseEntity<Object> handleValidationInternal(
      Exception ex,
      BindingResult bindingResult,
      HttpHeaders headers,
      HttpStatus httpStatus,
      WebRequest request) {

    ProblemType problemType = ProblemType.DADOS_INVALIDOS;

    String detail = MSG_DADOS_INVALIDOS;
    List<Problem.Object> problemObjects =
        bindingResult.getAllErrors().stream()
            .map(
                objectError -> {
                  String message =
                      messageSource.getMessage(objectError, LocaleContextHolder.getLocale());

                  String name = objectError.getObjectName();

                  if (objectError instanceof FieldError) {
                    name = ((FieldError) objectError).getField();
                  }

                  return Problem.Object.builder().name(name).userMessage(message).build();
                })
            .collect(Collectors.toList());

    Problem problem =
        createProblemBuilder(httpStatus, problemType, detail)
            .userMessage(detail)
            .objects(problemObjects)
            .build();
    return handleExceptionInternal(ex, problem, headers, httpStatus, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    if (ex instanceof MethodArgumentTypeMismatchException) {
      return handleMethodArgumentTypeMismatchException(
          (MethodArgumentTypeMismatchException) ex, headers, status, request);
    }
    return super.handleTypeMismatch(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
    String detail =
        String.format(
            "O recurso '%s', que voc?? tentou acessar, ?? inexistente!", ex.getRequestURL());
    Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();
    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  private ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
    String detail =
        String.format(
            "O par??metro de URL '%s' recebeu o valor '%s', que ?? de um tipo inv??lido. "
                + "Corrija e informe um valor compat??vel com o tipo %s!",
            ex.getName(),
            ex.getValue(),
            Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
    Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();
    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  private ResponseEntity<Object> handlePropertyBindingException(
      PropertyBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    String path = joinPath(ex.getPath());
    ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
    String detail =
        String.format(
            "A propriedade '%s' n??o existe. "
                + "Corrija ou remova essa propriedade e tente novamente.",
            path);
    Problem problem = createProblemBuilder(status, problemType, detail).userMessage(detail).build();
    return handleExceptionInternal(ex, problem, headers, status, request);
  }

  private ResponseEntity<Object> handleInvalidFormatException(
      InvalidFormatException ex, HttpHeaders headers, HttpStatus httpStatus, WebRequest request) {

    String path = joinPath(ex.getPath());

    ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
    String detail =
        String.format(
            "A propriedade '%s' recebeu o valor '%s',"
                + "que ?? de um tipo inv??lido. Corrija e informe um valor compat??vel com o tipo %s.",
            path, ex.getValue(), ex.getTargetType().getSimpleName());
    Problem problem =
        createProblemBuilder(httpStatus, problemType, detail).userMessage(detail).build();

    return handleExceptionInternal(ex, problem, headers, httpStatus, request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleUncaught(Exception ex, WebRequest request) {
    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
    String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

    log.error(ex.getMessage(), ex);
    ex.printStackTrace();
    Problem problem =
        createProblemBuilder(httpStatus, problemType, detail).userMessage(detail).build();
    return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatus, request);
  }

  @ExceptionHandler(ValidacaoException.class)
  public ResponseEntity<Object> handleValidacaoException(
      ValidacaoException ex, WebRequest request) {
    return handleValidationInternal(
        ex, ex.getBindingResult(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<?> handleAccessDeniedException(
      AccessDeniedException ex, WebRequest request) {
    HttpStatus httpStatus = HttpStatus.FORBIDDEN;
    ProblemType problemType = ProblemType.ACESSO_NEGADO;
    String detail = ex.getMessage();

    Problem problem =
        createProblemBuilder(httpStatus, problemType, detail)
            .userMessage("Voc?? n??o possui permiss??o para executar essa opera????o!")
            .build();

    return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatus, request);
  }

  @ExceptionHandler(EntidadeNaoEncontradaException.class)
  public ResponseEntity<?> handleEntidadeNaoEncontradaException(
      EntidadeNaoEncontradaException ex, WebRequest request) {
    HttpStatus httpStatus = HttpStatus.NOT_FOUND;
    ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
    String detail = ex.getMessage();

    Problem problem =
        createProblemBuilder(httpStatus, problemType, detail).userMessage(detail).build();

    return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatus, request);
  }

  @ExceptionHandler(EntidadeEmUsoException.class)
  public ResponseEntity<?> handleEntidadeEmUsoException(
      EntidadeEmUsoException ex, WebRequest request) {

    HttpStatus httpStatus = HttpStatus.CONFLICT;
    ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
    String detail = ex.getMessage();

    Problem problem =
        createProblemBuilder(httpStatus, problemType, detail).userMessage(detail).build();
    return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatus, request);
  }

  @ExceptionHandler(NegocioException.class)
  public ResponseEntity<?> handelNegocioException(NegocioException ex, WebRequest request) {
    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    ProblemType problemType = ProblemType.ERRO_NEGOCIO;
    String detail = ex.getMessage();

    Problem problem =
        createProblemBuilder(httpStatus, problemType, detail).userMessage(detail).build();

    return handleExceptionInternal(ex, problem, new HttpHeaders(), httpStatus, request);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

    if (body == null) {
      body =
          Problem.builder()
              .timestamp(OffsetDateTime.now())
              .title(status.getReasonPhrase())
              .status(status.value())
              .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
              .build();
    } else if (body instanceof String) {
      body =
          Problem.builder()
              .timestamp(OffsetDateTime.now())
              .title((String) body)
              .status(status.value())
              .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
              .build();
    }

    return super.handleExceptionInternal(ex, body, headers, status, request);
  }

  private Problem.ProblemBuilder createProblemBuilder(
      HttpStatus status, ProblemType problemType, String detail) {
    return Problem.builder()
        .timestamp(OffsetDateTime.now())
        .status(status.value())
        .type(problemType.getUri())
        .title(problemType.getTitle())
        .detail(detail);
  }

  private String joinPath(List<JsonMappingException.Reference> references) {
    return references.stream()
        .map(JsonMappingException.Reference::getFieldName)
        .collect(Collectors.joining("."));
  }
}
