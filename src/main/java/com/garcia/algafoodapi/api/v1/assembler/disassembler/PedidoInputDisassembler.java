package com.garcia.algafoodapi.api.v1.assembler.disassembler;

import com.garcia.algafoodapi.api.v1.model.input.PedidoInput;
import com.garcia.algafoodapi.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PedidoInputDisassembler {

  @Autowired private ModelMapper modelMapper;

  public Pedido toDomainObject(PedidoInput pedidoInput) {
    return modelMapper.map(pedidoInput, Pedido.class);
  }

  public void copyToDomainObject(PedidoInput pedidoInput, Pedido pedido) {
    modelMapper.map(pedidoInput, pedido);
  }
}
