package com.garcia.algafoodapi.api.v1.openapi.controller;

import com.garcia.algafoodapi.api.v1.model.UsuarioModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

@Tag(name = "Restaurantes")
@SecurityRequirement(name = "security_auth")
public interface RestauranteUsuarioControllerOpenApi {

  @Operation(
      summary = "Lista os responsáveis associados a um restaurantes",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "400",
            description = "ID do restaurante inválido",
            content = {@Content(schema = @Schema(ref = "Problema"))}),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante não encontrado",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  CollectionModel<UsuarioModel> listar(
      @Parameter(description = "ID do restaurante", example = "1", required = true)
          Long restauranteId);

  @Operation(
      summary = "Dissocia um usuário responsável de um restaurante",
      responses = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400",
            description = "ID do restaurante ou do usuário inválidos",
            content = {@Content(schema = @Schema(ref = "Problema"))}),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante ou usuário não encontrados",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  ResponseEntity<Void> dissociar(
      @Parameter(description = "ID do restaurante", example = "1", required = true)
          Long restauranteId,
      @Parameter(description = "ID do usuário responsável", example = "1", required = true)
          Long usuarioId);

  @Operation(
      summary = "Associa um usuário responsável a um restaurante",
      responses = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante ou usuário não encontrados",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  ResponseEntity<Void> associar(
      @Parameter(description = "ID do restaurante", example = "1", required = true)
          Long restauranteId,
      @Parameter(description = "ID do usuário responsável", example = "1", required = true)
          Long usuarioId);
}
