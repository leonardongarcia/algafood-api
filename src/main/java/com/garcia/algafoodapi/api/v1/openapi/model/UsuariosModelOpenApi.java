package com.garcia.algafoodapi.api.v1.openapi.model;

import com.garcia.algafoodapi.api.v1.model.UsuarioModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@Data
public class UsuariosModelOpenApi {

  private UsuariosEmbeddedModelOpenApi _embedded;
  private Links _links;

  @Data
  public class UsuariosEmbeddedModelOpenApi {

    private List<UsuarioModel> usuarios;
  }
}
