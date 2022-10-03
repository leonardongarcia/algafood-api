package com.garcia.algafoodapi.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoInput {

  @Schema(example = "Frango caipira")
  @NotBlank
  private String nome;

  @Schema(example = "Frango caipira ao modo de Minas Gerais")
  @NotBlank
  private String descricao;

  @Schema(example = "70")
  @PositiveOrZero
  @NotNull
  private BigDecimal preco;

  @Schema(example = "true")
  @NotNull
  private boolean ativo;
}
