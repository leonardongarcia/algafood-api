package com.garcia.algafoodapi.domain.exception;

public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException {

  public CidadeNaoEncontradaException(String message) {
    super(message);
  }

  public CidadeNaoEncontradaException(Long id) {
    this(String.format("Cidade com id %d não foi encontrada!", id));
  }
}
