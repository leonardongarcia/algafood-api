package com.garcia.algafoodapi.api.v1.assembler.disassembler;

import com.garcia.algafoodapi.api.v1.model.input.CidadeInput;
import com.garcia.algafoodapi.domain.model.Cidade;
import com.garcia.algafoodapi.domain.model.Estado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CidadeInputDisassembler {

  @Autowired private ModelMapper modelMapper;

  public Cidade toDomainObject(CidadeInput cidadeInput) {
    return modelMapper.map(cidadeInput, Cidade.class);
  }

  public void copyToDomainObject(CidadeInput cidadeInput, Cidade cidade) {
    cidade.setEstado(new Estado());
    modelMapper.map(cidadeInput, cidade);
  }
}
