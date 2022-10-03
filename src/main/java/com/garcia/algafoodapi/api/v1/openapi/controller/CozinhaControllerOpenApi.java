package com.garcia.algafoodapi.api.v1.openapi.controller;

import com.garcia.algafoodapi.api.v1.model.CozinhaModel;
import com.garcia.algafoodapi.api.v1.model.input.CozinhaInput;
import com.garcia.algafoodapi.core.springdoc.PageableParameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

@Tag(name = "Cozinhas")
@SecurityRequirement(name = "security_auth")
public interface CozinhaControllerOpenApi {

  @PageableParameter
  @Operation(summary = "Lista as cozinhas com paginação")
  PagedModel<CozinhaModel> listar(@Parameter(hidden = true) Pageable pageable);

  @Operation(summary = "Busca uma cozinha pelo ID", responses = {
          @ApiResponse(responseCode = "200"),
          @ApiResponse(
                  responseCode = "400",
                  description = "ID da cozinha inválido",
                  content = @Content(schema = @Schema(ref = "Problema"))),
          @ApiResponse(
                  responseCode = "404",
                  description = "Cozinha não encontrada",
                  content = @Content(schema = @Schema(ref = "Problema")))
  })
  CozinhaModel buscar(
      @Parameter(description = "ID da cozinha", example = "1", required = true) Long cozinhaId);

  @Operation(summary = "Cadastra uma cozinha", responses = {
          @ApiResponse(responseCode = "201", description = "Cozinha cadastrada")
  })
  CozinhaModel adicionar(
      @RequestBody(description = "Representação de uma cozinha", required = true)
          CozinhaInput cozinhaInput);

  @Operation(summary = "Atualiza uma cozinha",
          responses = {
                  @ApiResponse(responseCode = "200"),
                  @ApiResponse(
                          responseCode = "400",
                          description = "ID da cozinha inválido",
                          content = @Content(schema = @Schema(ref = "Problema"))),
                  @ApiResponse(
                          responseCode = "404",
                          description = "Cozinha não encontrada",
                          content = @Content(schema = @Schema(ref = "Problema")))
          })
  CozinhaModel atualizar(
      @Parameter(description = "ID da cozinha", example = "1", required = true) Long cozinhaId,
      @RequestBody(description = "Representação de uma cozinha", required = true)
          CozinhaInput cozinhaInput);

  @Operation(summary = "Exclui uma cozinha",
          responses = {
                  @ApiResponse(responseCode = "204"),
                  @ApiResponse(
                          responseCode = "400",
                          description = "ID da cozinha inválido",
                          content = @Content(schema = @Schema(ref = "Problema"))),
                  @ApiResponse(
                          responseCode = "404",
                          description = "Cozinha não encontrada",
                          content = @Content(schema = @Schema(ref = "Problema")))
          })
  void excluir(
      @Parameter(description = "ID da cozinha", example = "1", required = true) Long cozinhaId);
}
