package com.garcia.algafoodapi.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Relation(collectionRelation = "pedidos")
@Getter
@Setter
public class PedidoModel extends RepresentationModel<PedidoModel> {

  @Schema(example = "04813f77-79b5-11ec-9a17-0242ac1b0002")
  private String codigo;

  @Schema(example = "300.00")
  private BigDecimal subtotal;

  @Schema(example = "10.00")
  private BigDecimal taxaFrete;

  @Schema(example = "310.00")
  private BigDecimal valorTotal;

  @Schema(example = "ENTREGUE")
  private String status;

  @Schema(example = "2022-12-01T20:34:04Z")
  private OffsetDateTime dataCriacao;

  @Schema(example = "2022-12-01T20:34:04Z")
  private OffsetDateTime dataConfirmacao;

  @Schema(example = "2022-12-01T20:34:04Z")
  private OffsetDateTime dataEntrega;

  @Schema(example = "2022-12-01T20:34:04Z")
  private OffsetDateTime dataCancelamento;
  private RestauranteApenasNomeModel restaurante;
  private UsuarioModel cliente;
  private FormaPagamentoModel formaPagamento;
  private EnderecoModel enderecoEntrega;
  private List<ItemPedidoModel> itens;
}
