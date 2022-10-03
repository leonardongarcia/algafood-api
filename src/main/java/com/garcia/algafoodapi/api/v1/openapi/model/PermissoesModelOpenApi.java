package com.garcia.algafoodapi.api.v1.openapi.model;

import com.garcia.algafoodapi.api.v1.model.PermissaoModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@Data
public class PermissoesModelOpenApi {

  private PermissoesEmbeddedModelOpenApi _embedded;
  private Links _links;

  @Data
  public class PermissoesEmbeddedModelOpenApi {

    private List<PermissaoModel> permissoes;
  }
}
