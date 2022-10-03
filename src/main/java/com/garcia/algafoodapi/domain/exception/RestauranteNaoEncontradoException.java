package com.garcia.algafoodapi.domain.exception;

public class RestauranteNaoEncontradoException extends EntidadeNaoEncontradaException {

  public RestauranteNaoEncontradoException(String message) {
    super(message);
  }

  public RestauranteNaoEncontradoException(Long id) {
    this(String.format("Não existe cadastro de restaurante com o código %d", id));
  }
}
