package com.garcia.algafoodapi.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Relation(collectionRelation = "pedidos")
@Getter
@Setter
public class PedidoResumoModel extends RepresentationModel<PedidoResumoModel> {

  @Schema(example = "04813f77-79b5-11ec-9a17-0242ac1b0002")
  private String codigo;

  @Schema(example = "298.57")
  private BigDecimal subtotal;

  @Schema(example = "15.00")
  private BigDecimal taxaFrete;

  @Schema(example = "313.57")
  private BigDecimal valorTotal;

  @Schema(example = "ENTREGUE")
  private String status;

  @Schema(example = "2019-12-01T20:34:04Z")
  private OffsetDateTime dataCriacao;
  private RestauranteApenasNomeModel restaurante;
  private UsuarioModel cliente;
}
