package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.AlgaLinks;
import com.garcia.algafoodapi.api.v1.assembler.GrupoModelAssembler;
import com.garcia.algafoodapi.api.v1.model.GrupoModel;
import com.garcia.algafoodapi.api.v1.openapi.controller.UsuarioGrupoControllerOpenApi;
import com.garcia.algafoodapi.core.security.AlgaSecurity;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.model.Usuario;
import com.garcia.algafoodapi.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    path = "/v1/usuarios/{usuarioId}/grupos",
    produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioGrupoController implements UsuarioGrupoControllerOpenApi {

  @Autowired private CadastroUsuarioService cadastroUsuarioService;

  @Autowired private GrupoModelAssembler grupoModelAssembler;

  @Autowired private AlgaLinks algaLinks;

  @Autowired private AlgaSecurity algaSecurity;

  @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
  @Override
  @GetMapping
  public CollectionModel<GrupoModel> listar(@PathVariable Long usuarioId) {
    Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);

    CollectionModel<GrupoModel> gruposModel =
        grupoModelAssembler.toCollectionModel(usuario.getGrupos()).removeLinks();

    if (algaSecurity.podeEditarUsuariosGruposPermissoes()) {
      gruposModel.add(algaLinks.linkToUsuarioGrupoAssociacao(usuarioId, "associar"));

      gruposModel
          .getContent()
          .forEach(
              grupoModel -> {
                grupoModel.add(
                    algaLinks.linkToUsuarioGrupoDissociacao(
                        usuarioId, grupoModel.getId(), "dissociar"));
              });
    }

    return gruposModel;
  }

  @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
  @Override
  @DeleteMapping("/{grupoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> dissociar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
    cadastroUsuarioService.dissociarGrupo(usuarioId, grupoId);
    return ResponseEntity.noContent().build();
  }

  @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
  @Override
  @PutMapping("/{grupoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> associar(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
    cadastroUsuarioService.associarGrupo(usuarioId, grupoId);
    return ResponseEntity.noContent().build();
  }
}
