package com.garcia.algafoodapi.api.v1.openapi.controller;

import com.garcia.algafoodapi.api.v1.model.FormaPagamentoModel;
import com.garcia.algafoodapi.api.v1.model.input.FormaPagamentoInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

@Tag(name = "Formas de Pagamento")
@SecurityRequirement(name = "security_auth")
public interface FormaPagamentoControllerOpenApi {

  @Operation(summary = "Lista as formas de pagamento")
  ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(
      @Parameter(hidden = true) ServletWebRequest request);

  @Operation(
      summary = "Busca uma forma de pagamento pelo ID",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "400",
            description = "ID da forma de pagamento inválido",
            content = @Content(schema = @Schema(ref = "Problema"))),
        @ApiResponse(
            responseCode = "404",
            description = "Forma de pagamento não encontrada",
            content = @Content(schema = @Schema(ref = "Problema")))
      })
  ResponseEntity<FormaPagamentoModel> buscar(
      @Parameter(description = "ID  da forma de pagamento", example = "1", required = true)
          Long formaPagamentoId,
      ServletWebRequest request);

  @Operation(
      summary = "Cadastra uma forma de pagamento",
      responses = {
        @ApiResponse(responseCode = "201", description = "Forma de pagamento cadastrada")
      })
  FormaPagamentoModel save(
      @RequestBody(description = "Representação de uma forma de pagamento", required = true)
          FormaPagamentoInput formaPagamentoInput);

  @Operation(
      summary = "Altera uma forma de pagamento",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "400",
            description = "ID da forma de pagamento inválido",
            content = @Content(schema = @Schema(ref = "Problema"))),
        @ApiResponse(
            responseCode = "404",
            description = "Forma de pagamento não encontrada",
            content = @Content(schema = @Schema(ref = "Problema")))
      })
  FormaPagamentoModel alterar(
      @Parameter(description = "ID  da forma de pagamento", example = "1", required = true)
          Long formaPagamentoId,
      @RequestBody(description = "Representação de uma forma de pagamento", required = true)
          FormaPagamentoInput formaPagamentoInput);

  @Operation(
      summary = "Exclui uma forma de pagamento",
      responses = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400",
            description = "ID da forma de pagamento inválido",
            content = @Content(schema = @Schema(ref = "Problema"))),
        @ApiResponse(
            responseCode = "404",
            description = "Forma de pagamento não encontrada",
            content = @Content(schema = @Schema(ref = "Problema")))
      })
  void excluir(
      @Parameter(description = "ID  da forma de pagamento", example = "1", required = true)
          Long formaPagamentoId);
}
