package com.garcia.algafoodapi.api.v1.assembler;

import com.garcia.algafoodapi.api.v1.AlgaLinks;
import com.garcia.algafoodapi.api.v1.controller.CozinhaController;
import com.garcia.algafoodapi.api.v1.model.CozinhaModel;
import com.garcia.algafoodapi.core.security.AlgaSecurity;
import com.garcia.algafoodapi.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CozinhaModelAssembler extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModel> {

  @Autowired private ModelMapper modelMapper;
  @Autowired private AlgaLinks algaLinks;
  @Autowired
  private AlgaSecurity algaSecurity;



  public CozinhaModelAssembler(){
    super(CozinhaController.class, CozinhaModel.class);
  }

  @Override
  public CozinhaModel toModel(Cozinha cozinha) {
    CozinhaModel cozinhaModel = createModelWithId(cozinha.getId(), cozinha);
    modelMapper.map(cozinha, cozinhaModel);

    if (algaSecurity.podeConsultarCozinhas()) {
      cozinhaModel.add(algaLinks.linkToCozinhas("cozinhas"));
    }

    return cozinhaModel;
  }
}
