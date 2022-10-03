package com.garcia.algafoodapi.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioComSenhaInput extends UsuarioInput {

  @Schema(example = "123", type = "string")
  @NotBlank
  private String senha;
}
