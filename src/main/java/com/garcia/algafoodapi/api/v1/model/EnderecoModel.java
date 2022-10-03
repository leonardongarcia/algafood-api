package com.garcia.algafoodapi.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoModel {

  @Schema(example = "38425-745")
  private String cep;

  @Schema(example = "Rua Floriano Peixoto")
  private String logradouro;

  @Schema(example = "1500")
  private String numero;

  @Schema(example = "AP 404")
  private String complemento;

  @Schema(example = "Centro")
  private String bairro;

  private CidadeResumoModel cidade;
}
