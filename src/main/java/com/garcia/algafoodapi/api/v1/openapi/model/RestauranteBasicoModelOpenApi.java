package com.garcia.algafoodapi.api.v1.openapi.model;

import com.garcia.algafoodapi.api.v1.model.CozinhaModel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class RestauranteBasicoModelOpenApi {

  private Long id;

  private String nome;

  private BigDecimal taxaFrete;

  private CozinhaModel cozinha;
}
