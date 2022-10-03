package com.garcia.algafoodapi.api.v1.openapi.controller;

import com.garcia.algafoodapi.api.v1.model.FotoProdutoModel;
import com.garcia.algafoodapi.api.v1.model.input.FotoProdutoInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import java.io.IOException;

@Tag(name = "Produtos")
@SecurityRequirement(name = "security_auth")
public interface RestauranteProdutoFotoControllerOpenApi {

  @Operation(
      summary = "Atualiza a foto do produto de um restaurante",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "404",
            description = "ID do produto ou restaurante não encontrados",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  FotoProdutoModel atualizarFoto(
      @Parameter(description = "ID do restaurante", example = "1", required = true)
          Long restauranteId,
      @Parameter(description = "ID do produto", example = "2", required = true) Long produtoId,
      @RequestBody(required = true) FotoProdutoInput fotoProdutoInput)
      throws IOException;

  @Operation(
      summary = "Busca a foto do produto de um restaurante",
      responses = {
        @ApiResponse(
            responseCode = "200",
            content = {
              @Content(
                  mediaType = "application/json",
                  schema = @Schema(type = "string", format = "binary")),
              @Content(
                  mediaType = "image/jpeg",
                  schema = @Schema(type = "string", format = "binary")),
              @Content(
                  mediaType = "image/png",
                  schema = @Schema(type = "string", format = "binary"))
            }),
        @ApiResponse(
            responseCode = "400",
            description = "ID do produto ou restaurante inválidos",
            content = {@Content(schema = @Schema(ref = "Problema"))}),
        @ApiResponse(
            responseCode = "404",
            description = "ID do produto ou restaurante não encontrados",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  ResponseEntity<?> buscar(
      @Parameter(description = "ID do restaurante", example = "1", required = true)
          Long restauranteId,
      @Parameter(description = "ID do produto", example = "1", required = true) Long produtoId,
      @Parameter(hidden = true) String acceptHeader)
      throws HttpMediaTypeNotAcceptableException;

  @Operation(
      summary = "Exclui a foto do produto de um restaurante",
      responses = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400",
            description = "ID do produto ou restaurante inválidos",
            content = {@Content(schema = @Schema(ref = "Problema"))}),
        @ApiResponse(
            responseCode = "404",
            description = "ID do produto ou restaurante não encontrados",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  void delete(
      @Parameter(description = "ID do restaurante", example = "1", required = true)
          Long restauranteId,
      @Parameter(description = "ID do produto", example = "1", required = true) Long produtoId);

  //  @ApiOperation(value = "Busca a foto do produto de um restaurante", hidden = true)
  //  ResponseEntity<?> servirFoto(Long restauranteId, Long produtoId, String acceptHeader)
  //      throws HttpMediaTypeNotAcceptableException;
}
