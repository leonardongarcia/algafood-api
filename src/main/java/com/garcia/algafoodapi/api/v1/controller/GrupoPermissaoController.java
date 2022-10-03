package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.AlgaLinks;
import com.garcia.algafoodapi.api.v1.assembler.PermissaoModelAssembler;
import com.garcia.algafoodapi.api.v1.model.PermissaoModel;
import com.garcia.algafoodapi.api.v1.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.garcia.algafoodapi.core.security.AlgaSecurity;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.model.Grupo;
import com.garcia.algafoodapi.domain.service.CadastroGrupoService;
import com.garcia.algafoodapi.domain.service.CadastroPermissaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    path = "/v1/grupos/{grupoId}/permissoes",
    produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi {

  @Autowired private CadastroPermissaoService cadastroPermissaoService;

  @Autowired private CadastroGrupoService cadastroGrupoService;

  @Autowired private PermissaoModelAssembler permissaoModelAssembler;

  @Autowired private AlgaSecurity algaSecurity;

  @Autowired private AlgaLinks algaLinks;

  @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
  @Override
  @GetMapping
  public CollectionModel<PermissaoModel> listar(@PathVariable Long grupoId) {
    Grupo grupo = cadastroGrupoService.buscarOuFalhar(grupoId);

    CollectionModel<PermissaoModel> permissoesModel =
        permissaoModelAssembler.toCollectionModel(grupo.getPermissoes()).removeLinks();

    permissoesModel.add(algaLinks.linkToGrupoPermissoes(grupoId));

    if (algaSecurity.podeEditarUsuariosGruposPermissoes()) {
      permissoesModel.add(algaLinks.linkToGrupoPermissaoAssociacao(grupoId, "associar"));

      permissoesModel
          .getContent()
          .forEach(
              permissaoModel -> {
                permissaoModel.add(
                    algaLinks.linkToGrupoPermissaoDissociacao(
                        grupoId, permissaoModel.getId(), "dissociar"));
              });
    }

    return permissoesModel;
  }

  @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
  @Override
  @DeleteMapping("/{permissaoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> dissociar(
      @PathVariable Long grupoId, @PathVariable Long permissaoId) {
    cadastroGrupoService.dissociarPermissao(grupoId, permissaoId);

    return ResponseEntity.noContent().build();
  }

  @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
  @Override
  @PutMapping("/{permissaoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> associar(@PathVariable Long grupoId, @PathVariable Long permissaoId) {
    cadastroGrupoService.associarPermissao(grupoId, permissaoId);

    return ResponseEntity.noContent().build();
  }
}
