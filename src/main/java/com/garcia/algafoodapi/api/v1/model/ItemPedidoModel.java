package com.garcia.algafoodapi.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemPedidoModel extends RepresentationModel<ItemPedidoModel> {

  @Schema(example = "1")
  private Long produtoId;

  @Schema(example = "Frango caipira")
  private String produtoNome;

  @Schema(example = "1")
  private Integer quantidade;

  @Schema(example = "70.00")
  private BigDecimal precoUnitario;

  @Schema(example = "70.00")
  private BigDecimal precoTotal;

  @Schema(example = "Com muito coentro")
  private String observacao;
}
