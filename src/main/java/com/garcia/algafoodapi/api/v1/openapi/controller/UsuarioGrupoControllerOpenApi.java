package com.garcia.algafoodapi.api.v1.openapi.controller;

import com.garcia.algafoodapi.api.v1.model.GrupoModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

@Tag(name = "Usuários")
@SecurityRequirement(name = "security_auth")
public interface UsuarioGrupoControllerOpenApi {

  @Operation(summary = "Lista os grupos que um usuário está inserido")
  CollectionModel<GrupoModel> listar(
      @Parameter(description = "ID do usuário", example = "1") Long usuarioId);

  @Operation(summary = "Dissocia um usuário de um grupo")
  ResponseEntity<Void> dissociar(
      @Parameter(description = "ID do usuário", example = "1") Long usuarioId,
      @Parameter(description = "ID do grupo", example = "1") Long grupoId);

  @Operation(summary = "Associa um usuário a um grupo")
  ResponseEntity<Void> associar(
      @Parameter(description = "ID do usuário", example = "1") Long usuarioId,
      @Parameter(description = "ID do grupo", example = "1") Long grupoId);
}
