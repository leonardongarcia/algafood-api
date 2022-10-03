package com.garcia.algafoodapi.api.v2.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "cozinhas")
@Getter
@Setter
public class CozinhaModelV2 extends RepresentationModel<CozinhaModelV2> {

  private Long idCozinha;

  private String nomeCozinha;
}
