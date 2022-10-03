package com.garcia.algafoodapi.domain.listener;

import com.garcia.algafoodapi.domain.event.PedidoCanceladoEvent;
import com.garcia.algafoodapi.domain.model.Pedido;
import com.garcia.algafoodapi.domain.service.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificacaoClientePedidoCanceladoListener {

  @Autowired private EnvioEmailService envioEmailService;

  @TransactionalEventListener
  public void aoCancelarPedido(PedidoCanceladoEvent pedidoCanceladoEvent) {
    Pedido pedido = pedidoCanceladoEvent.getPedido();

    var mensagem =
        EnvioEmailService.Mensagem.builder()
            .assunto(pedido.getRestaurante().getNome() + " - Pedido cancelado")
            .corpo("emails/pedido-cancelado.html")
            .variavel("pedido", pedido)
            .destinatario(pedido.getCliente().getEmail())
            .build();

    envioEmailService.enviar(mensagem);
  }
}
