package com.garcia.algafoodapi.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Relation(collectionRelation = "restaurantes")
@Setter
@Getter
public class RestauranteModel extends RepresentationModel<RestauranteModel> {

  @Schema(example = "1")
  private Long id;

  @Schema(example = "Tradição Mineira")
  private String nome;

  @Schema(example = "10")
  private BigDecimal taxaFrete;

  @Schema(example = "Mineira")
  private CozinhaModel cozinha;

  @Schema(example = "true")
  private Boolean ativo;

  @Schema(example = "true")
  private Boolean aberto;

  private EnderecoModel endereco;
}
