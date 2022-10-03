package com.garcia.algafoodapi.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class ItemPedidoInput {

  @Schema(example = "1")
  @NotNull
  private Long produtoId;

  @Schema(example = "2")
  @NotNull
  @PositiveOrZero
  private Integer quantidade;

  @Schema(example = "Com muita cebola")
  private String observacao;
}
