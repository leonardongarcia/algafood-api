package com.garcia.algafoodapi.api.v1.assembler.disassembler;

import com.garcia.algafoodapi.api.v1.model.input.FormaPagamentoInput;
import com.garcia.algafoodapi.domain.model.FormaPagamento;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormaPagamentoInputDisassembler {

  @Autowired private ModelMapper modelMapper;

  public FormaPagamento toObjectModel(FormaPagamentoInput formaPagamentoInput) {
    return modelMapper.map(formaPagamentoInput, FormaPagamento.class);
  }

  public void copyToDomainObject(
      FormaPagamentoInput formaPagamentoInput, FormaPagamento formaPagamento) {
    modelMapper.map(formaPagamentoInput, formaPagamento);
  }
}
