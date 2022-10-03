package com.garcia.algafoodapi.api.v2.assembler;

import com.garcia.algafoodapi.api.v2.AlgaLinksV2;
import com.garcia.algafoodapi.api.v2.controller.CozinhaControllerV2;
import com.garcia.algafoodapi.api.v2.model.CozinhaModelV2;
import com.garcia.algafoodapi.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CozinhaModelAssemblerV2 extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModelV2> {

  @Autowired private ModelMapper modelMapper;
  @Autowired private AlgaLinksV2 algaLinksV2;



  public CozinhaModelAssemblerV2(){
    super(CozinhaControllerV2.class, CozinhaModelV2.class);
  }

  @Override
  public CozinhaModelV2 toModel(Cozinha cozinha) {
    CozinhaModelV2 cozinhaModelV2 = createModelWithId(cozinha.getId(), cozinha);
    modelMapper.map(cozinha, cozinhaModelV2);

    cozinhaModelV2.add(algaLinksV2.linkToCozinhas("cozinhas"));

    return cozinhaModelV2;
  }
}
