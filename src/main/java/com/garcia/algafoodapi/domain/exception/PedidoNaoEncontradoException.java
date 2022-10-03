package com.garcia.algafoodapi.domain.exception;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException {

  public PedidoNaoEncontradoException(String codigoPedido) {
    super(String.format("Não existe um cadastro de pedido com o código %s", codigoPedido));
  }
}
