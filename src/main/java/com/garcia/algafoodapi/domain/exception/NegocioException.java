package com.garcia.algafoodapi.domain.exception;

// @ResponseStatus(HttpStatus.BAD_REQUEST)
public class NegocioException extends RuntimeException {

  public NegocioException(String message) {
    super(message);
  }

  public NegocioException(String mensagem, Throwable cause) {
    super(mensagem, cause);
  }
}
