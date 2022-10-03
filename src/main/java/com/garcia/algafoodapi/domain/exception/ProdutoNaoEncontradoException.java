package com.garcia.algafoodapi.domain.exception;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaException {
  public ProdutoNaoEncontradoException(String message) {
    super(message);
  }

  public ProdutoNaoEncontradoException(Long restauranteId, Long produtoId) {
    this(
        String.format(
            "Não existe um cadastro de produto com código %d para o restaurante de código %d",
            produtoId, restauranteId));
  }
}
