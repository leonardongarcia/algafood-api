package com.garcia.algafoodapi.api.v1.openapi.controller;

import com.garcia.algafoodapi.api.v1.model.PermissaoModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

@Tag(name = "Grupos")
@SecurityRequirement(name = "security_auth")
public interface GrupoPermissaoControllerOpenApi {

  @Operation(
      summary = "Lista as permissões de um grupo",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "400",
            description = "ID do grupo inválido",
            content = {@Content(schema = @Schema(ref = "Problema"))}),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo não encontrado",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  CollectionModel<PermissaoModel> listar(
      @Parameter(description = "ID do grupo", example = "1", required = true) Long grupoId);

  @Operation(
      summary = "Dissocia uma permissão de um grupo",
      responses = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400",
            description = "ID do grupo ou da permissão inválidos",
            content = {@Content(schema = @Schema(ref = "Problema"))}),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo ou permissão não encontrados",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  ResponseEntity<Void> dissociar(
      @Parameter(description = "ID do grupo", example = "1", required = true) Long grupoId,
      @Parameter(description = "ID do permissão", example = "1", required = true) Long permissaoId);

  @Operation(
      summary = "Associa uma permissão a um grupo",
      responses = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo ou permissão não encontrados",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  ResponseEntity<Void> associar(
      @Parameter(description = "ID do grupo", example = "1", required = true) Long grupoId,
      @Parameter(description = "ID do permissão", example = "1", required = true) Long permissaoId);
}
