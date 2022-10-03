package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.model.Pedido;
import com.garcia.algafoodapi.domain.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FluxoPedidoService {

  @Autowired private EmissaoPedidoService emissaoPedidoService;

  @Autowired private PedidoRepository pedidoRepository;

  @Transactional
  public void confirmar(String codigoPedido) {
    Pedido pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
    pedido.confirmar();

    pedidoRepository.save(pedido);
  }

  @Transactional
  public void cancelar(String codigoPedido) {
    Pedido pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
    pedido.cancelar();
    pedidoRepository.save(pedido);
  }

  @Transactional
  public void entregar(String codigoPedido) {
    Pedido pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
    pedido.entregar();
  }
}
