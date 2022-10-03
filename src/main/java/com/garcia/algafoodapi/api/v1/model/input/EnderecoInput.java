package com.garcia.algafoodapi.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EnderecoInput {

  @Schema(example = "38425-745")
  @NotBlank private String cep;

  @Schema(example = "Av. João Naves de Ávila")
  @NotBlank private String logradouro;

  @Schema(example = "125")
  @NotBlank private String numero;

  @Schema(example = "Ap. 404")
  private String complemento;

  @Schema(example = "Centro")
  @NotBlank private String bairro;

  @Valid @NotNull private CidadeIdInput cidade;
}
