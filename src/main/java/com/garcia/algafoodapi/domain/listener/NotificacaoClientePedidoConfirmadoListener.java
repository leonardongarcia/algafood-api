package com.garcia.algafoodapi.domain.listener;

import com.garcia.algafoodapi.domain.event.PedidoConfirmadoEvent;
import com.garcia.algafoodapi.domain.model.Pedido;
import com.garcia.algafoodapi.domain.service.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificacaoClientePedidoConfirmadoListener {

  @Autowired private EnvioEmailService envioEmailService;

  @TransactionalEventListener
  public void aoConfirmarPedido(PedidoConfirmadoEvent event) {
    Pedido pedido = event.getPedido();

    var mensagem =
        EnvioEmailService.Mensagem.builder()
            .assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado")
            .corpo("emails/pedido-confirmado.html")
            .variavel("pedido", pedido)
            .destinatario(pedido.getCliente().getEmail())
            .build();

    envioEmailService.enviar(mensagem);
  }
}
