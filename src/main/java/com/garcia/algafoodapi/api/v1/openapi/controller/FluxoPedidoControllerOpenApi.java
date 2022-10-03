package com.garcia.algafoodapi.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Pedidos")
@SecurityRequirement(name = "security_auth")
public interface FluxoPedidoControllerOpenApi {

  @Operation(
      summary = "Confirma um pedido",
      responses = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "Pedido não encontrado",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  ResponseEntity<Void> confirmar(
      @Parameter(
              description = "Código do pedido",
              example = "04813f77-79b5-11ec-9a17-0242ac1b0002",
              required = true)
          String codigoPedido);

  @Operation(
      summary = "Cancela um pedido",
      responses = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "Pedido não encontrado",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  ResponseEntity<Void> cancelar(
      @Parameter(
              description = "Código do pedido",
              example = "04813f77-79b5-11ec-9a17-0242ac1b0002",
              required = true)
          String codigoPedido);

  @Operation(
      summary = "Registra a entrega de um pedido",
      responses = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "Pedido não encontrado",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  ResponseEntity<Void> entregar(
      @Parameter(
              description = "Código do pedido",
              example = "04813f77-79b5-11ec-9a17-0242ac1b0002",
              required = true)
          String codigoPedido);
}
