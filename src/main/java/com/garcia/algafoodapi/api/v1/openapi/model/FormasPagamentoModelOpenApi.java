package com.garcia.algafoodapi.api.v1.openapi.model;

import com.garcia.algafoodapi.api.v1.model.FormaPagamentoModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@Data
public class FormasPagamentoModelOpenApi {

  private FormasPagamentoEmbeddedModelOpenApi _embedded;
  private Links _links;

  @Data
  public class FormasPagamentoEmbeddedModelOpenApi {

    private List<FormaPagamentoModel> formasPagamento;
  }
}
