package com.garcia.algafoodapi.api.v2.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CidadeInputV2 {

  @NotBlank private String nomeCidade;

  private Long idEstado;
}
