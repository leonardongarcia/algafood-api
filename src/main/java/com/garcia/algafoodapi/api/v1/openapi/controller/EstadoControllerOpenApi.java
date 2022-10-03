package com.garcia.algafoodapi.api.v1.openapi.controller;

import com.garcia.algafoodapi.api.v1.model.EstadoModel;
import com.garcia.algafoodapi.api.v1.model.input.EstadoInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;

@Tag(name = "Estados")
@SecurityRequirement(name = "security_auth")
public interface EstadoControllerOpenApi {

  @Operation(summary = "Lista os estados")
  CollectionModel<EstadoModel> listar();

  @Operation(
      summary = "Busca um estado por ID",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "400",
            description = "ID do estado inválido",
            content = {@Content(schema = @Schema(ref = "Problema"))}),
        @ApiResponse(
            responseCode = "404",
            description = "Estado não encontrado",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  EstadoModel buscar(
      @Parameter(description = "ID do estado", example = "1", required = true) Long estadoId);

  @Operation(
      summary = "Cadastra um estado",
      responses = {@ApiResponse(responseCode = "201", description = "Estado cadastrado")})
  EstadoModel adicionar(
      @RequestBody(description = "Representação de um estado", required = true)
          EstadoInput estadoInput);

  @Operation(
      summary = "Atualiza um estado",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "404",
            description = "Estado não encontrado",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  EstadoModel atualizar(
      @Parameter(description = "ID do estado", example = "1", required = true) Long estadoId,
      @RequestBody(description = "Representação de um estado", required = true)
          EstadoInput estadoInput);

  @Operation(
      summary = "Exclui um estado",
      responses = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400",
            description = "ID do estado inválido",
            content = {@Content(schema = @Schema(ref = "Problema"))}),
        @ApiResponse(
            responseCode = "404",
            description = "Estado não encontrado",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  void excluir(
      @Parameter(description = "ID do estado", example = "1", required = true) Long estadoId);
}
