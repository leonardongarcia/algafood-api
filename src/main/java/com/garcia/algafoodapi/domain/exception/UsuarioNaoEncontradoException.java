package com.garcia.algafoodapi.domain.exception;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaException {
  public UsuarioNaoEncontradoException(String message) {
    super(message);
  }

  public UsuarioNaoEncontradoException(Long usuarioId) {
    this(String.format("Não existe um usuário cadastrado com o código %d", usuarioId));
  }
}
