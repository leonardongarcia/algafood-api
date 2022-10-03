package com.garcia.algafoodapi.domain.exception;

public class GrupoNaoEncontradoException extends EntidadeNaoEncontradaException {
  public GrupoNaoEncontradoException(String message) {
    super(message);
  }

  public GrupoNaoEncontradoException(Long grupoId) {
    this(String.format("Não existe um grupo com o código %d", grupoId));
  }
}
