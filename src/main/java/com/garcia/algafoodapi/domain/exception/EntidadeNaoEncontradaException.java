package com.garcia.algafoodapi.domain.exception;

// @ResponseStatus(HttpStatus.NOT_FOUND)
public abstract class EntidadeNaoEncontradaException extends NegocioException {

  public EntidadeNaoEncontradaException(String message) {
    super(message);
  }
}
