package com.garcia.algafoodapi.api.v1.openapi.controller;

import com.garcia.algafoodapi.api.v1.model.UsuarioModel;
import com.garcia.algafoodapi.api.v1.model.input.SenhaInput;
import com.garcia.algafoodapi.api.v1.model.input.UsuarioComSenhaInput;
import com.garcia.algafoodapi.api.v1.model.input.UsuarioInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;

@Tag(name = "Usuários")
@SecurityRequirement(name = "security_auth")
public interface UsuarioControllerOpenApi {

  @Operation(summary = "Lista os usuários")
  CollectionModel<UsuarioModel> listar();

  @Operation(
      summary = "Busca um usuário por ID",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "400",
            description = "ID do usuário inválido",
            content = {@Content(schema = @Schema(ref = "Problema"))}),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  UsuarioModel buscar(
      @Parameter(description = "ID do usuário", example = "1", required = true) Long usuarioId);

  @Operation(
      summary = "Cadastra um usuário",
      responses = {@ApiResponse(responseCode = "201", description = "Usuário cadastrado")})
  UsuarioModel adicionar(
      @RequestBody(description = "Representação de um usuário com senha", required = true)
          UsuarioComSenhaInput usuarioComSenhaInput);

  @Operation(
      summary = "Atualiza um usuário",
      responses = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  UsuarioModel atualizar(
      @Parameter(description = "ID do usuário", example = "1", required = true) Long usuarioId,
      @RequestBody(description = "Representação de um usuário", required = true)
          UsuarioInput usuarioInput);

  @Operation(
      summary = "Altera a senha de um usuário",
      responses = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado",
            content = {@Content(schema = @Schema(ref = "Problema"))})
      })
  void alterarSenha(
      @Parameter(description = "ID do usuário", example = "1", required = true) Long usuarioId,
      @RequestBody(description = "Representação para alteração de senha", required = true)
          SenhaInput senhaInput);
}
