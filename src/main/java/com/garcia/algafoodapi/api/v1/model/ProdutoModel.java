package com.garcia.algafoodapi.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@Relation(collectionRelation = "produtos")
@Setter
@Getter
public class ProdutoModel extends RepresentationModel<ProdutoModel> {

  @Schema(example = "1")
  private Long id;

  @Schema(example = "Frango caipira")
  private String nome;

  @Schema(example = "Frango caipira ao modo de Minas Gerais")
  private String descricao;

  @Schema(example = "70")
  private BigDecimal preco;

  @Schema(example = "true")
  private boolean ativo;
}
