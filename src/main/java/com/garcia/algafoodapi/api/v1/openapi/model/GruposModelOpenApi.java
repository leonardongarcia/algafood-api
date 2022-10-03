package com.garcia.algafoodapi.api.v1.openapi.model;

import com.garcia.algafoodapi.api.v1.model.GrupoModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@Data
public class GruposModelOpenApi {

  private GruposEmbeddedModelOpenApi _embedded;
  private Links _links;

  @Data
  public class GruposEmbeddedModelOpenApi {

    private List<GrupoModel> grupos;
  }
}
