package com.garcia.algafoodapi.api.v2.openapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageModelV2OpenApi {

  private Long size;

  private Long totalElements;

  private Long totalPages;

  private Long number;
}
