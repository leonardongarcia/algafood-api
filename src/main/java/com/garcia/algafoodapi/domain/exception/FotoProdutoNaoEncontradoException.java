package com.garcia.algafoodapi.domain.exception;

public class FotoProdutoNaoEncontradoException extends EntidadeNaoEncontradaException {

  public FotoProdutoNaoEncontradoException(String message) {
    super(message);
  }

  public FotoProdutoNaoEncontradoException(Long produtoId, Long restauranteId) {
    this(
        String.format(
            "Não existe um cadastro de de foto para o produto com código %d para o"
                + " restaurante de código %d",
            produtoId, restauranteId));
  }
}
