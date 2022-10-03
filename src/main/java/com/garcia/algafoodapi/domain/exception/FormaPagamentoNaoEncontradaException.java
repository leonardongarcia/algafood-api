package com.garcia.algafoodapi.domain.exception;

public class FormaPagamentoNaoEncontradaException extends EntidadeNaoEncontradaException {

  public FormaPagamentoNaoEncontradaException(String message) {
    super(message);
  }

  public FormaPagamentoNaoEncontradaException(Long id) {
    this(String.format("Não foi encontrada uma forma de pagamento com o código %d", id));
  }
}
