package com.garcia.algafoodapi.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioInput {

  @Schema(example = "Leonardo")
  @NotBlank
  private String nome;

  @Schema(example = "leonprogramacao@gmail.com")
  @Email
  @NotBlank
  private String email;
}
