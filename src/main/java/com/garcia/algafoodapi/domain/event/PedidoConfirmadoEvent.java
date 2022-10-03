package com.garcia.algafoodapi.domain.event;

import com.garcia.algafoodapi.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PedidoConfirmadoEvent {

  private Pedido pedido;
}
