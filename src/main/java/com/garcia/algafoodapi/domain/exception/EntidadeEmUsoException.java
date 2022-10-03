package com.garcia.algafoodapi.domain.exception;

// @ResponseStatus(HttpStatus.CONFLICT)
public class EntidadeEmUsoException extends NegocioException {

  public EntidadeEmUsoException(String message) {
    super(message);
  }
}
