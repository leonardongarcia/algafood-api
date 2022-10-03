package com.garcia.algafoodapi.api.v2.model.input;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CozinhaInputV2 {

  @NotBlank private String nome;
}
