package com.garcia.algafoodapi.api.v1.assembler.disassembler;

import com.garcia.algafoodapi.api.v1.model.input.UsuarioInput;
import com.garcia.algafoodapi.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioInputDisassembler {

  @Autowired private ModelMapper modelMapper;

  public Usuario toDomainObject(UsuarioInput usuarioInput) {
    return modelMapper.map(usuarioInput, Usuario.class);
  }

  public void copyToDomainObject(UsuarioInput usuarioInput, Usuario usuario) {
    modelMapper.map(usuarioInput, usuario);
  }
}
