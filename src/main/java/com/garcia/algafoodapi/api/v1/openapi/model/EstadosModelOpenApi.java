package com.garcia.algafoodapi.api.v1.openapi.model;

import com.garcia.algafoodapi.api.v1.model.EstadoModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@Data
public class EstadosModelOpenApi {

  private EstadosEmbeddedModelOpenApi _embedded;
  private Links _links;

  @Data
  public class EstadosEmbeddedModelOpenApi {

    private List<EstadoModel> estados;
  }
}
