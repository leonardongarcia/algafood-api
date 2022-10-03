package com.garcia.algafoodapi.api.v1.openapi.model;

import com.garcia.algafoodapi.api.v1.model.CozinhaModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Links;

import java.util.List;

@Setter
@Getter
public class CozinhasModelOpenApi {

  private CozinhasEmbeddedModelOpenApi _embedded;
  private Links _links;
  private PageModelOpenApi page;

  @Data
  public static class CozinhasEmbeddedModelOpenApi {
    private List<CozinhaModel> cozinhas;
  }
}
