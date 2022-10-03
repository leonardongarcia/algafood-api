package com.garcia.algafoodapi.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
public class RestauranteInput {

  @Schema(example = "Tradição Mineira")
  @NotBlank
  private String nome;

  @Schema(example = "10")
  @PositiveOrZero
  @NotNull
  private BigDecimal taxaFrete;

  @Valid @NotNull private CozinhaIdInput cozinha;

  @Valid @NotNull private EnderecoInput endereco;
}
