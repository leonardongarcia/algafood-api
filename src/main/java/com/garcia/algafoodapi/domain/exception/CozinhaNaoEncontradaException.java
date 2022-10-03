package com.garcia.algafoodapi.domain.exception;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

  public CozinhaNaoEncontradaException(String message) {
    super(message);
  }

  public CozinhaNaoEncontradaException(Long id) {
    this(String.format("Não existe cadastro de cozinha com o código %d", id));
  }
}
