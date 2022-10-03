package com.garcia.algafoodapi.api.v1.assembler.disassembler;

import com.garcia.algafoodapi.api.v1.model.input.PermissaoInput;
import com.garcia.algafoodapi.domain.model.Permissao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PermissaoInputDisassembler {

  @Autowired private ModelMapper modelMapper;

  public Permissao toDomainObject(PermissaoInput permissaoInput) {
    return modelMapper.map(permissaoInput, Permissao.class);
  }

  public void toCopyDomainObject(PermissaoInput permissaoInput, Permissao permissao) {
    modelMapper.map(permissaoInput, permissao);
  }
}
