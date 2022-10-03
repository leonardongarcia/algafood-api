package com.garcia.algafoodapi.api.v2.assembler;

import com.garcia.algafoodapi.api.v2.model.input.CidadeInputV2;
import com.garcia.algafoodapi.domain.model.Cidade;
import com.garcia.algafoodapi.domain.model.Estado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CidadeInputDisassemblerV2 {

    @Autowired
    private ModelMapper modelMapper;

    public Cidade toDomainObject(CidadeInputV2 cidadeInput) {
        return modelMapper.map(cidadeInput, Cidade.class);
    }

    public void copyToDomainObject(CidadeInputV2 cidadeInput, Cidade cidade) {
       cidade.setEstado(new Estado());
       modelMapper.map(cidadeInput, cidade);
    }
}
