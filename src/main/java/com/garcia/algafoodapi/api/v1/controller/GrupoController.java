package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.assembler.GrupoModelAssembler;
import com.garcia.algafoodapi.api.v1.assembler.disassembler.GrupoInputDisassembler;
import com.garcia.algafoodapi.api.v1.model.GrupoModel;
import com.garcia.algafoodapi.api.v1.model.input.GrupoInput;
import com.garcia.algafoodapi.api.v1.openapi.controller.GrupoControllerOpenApi;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.model.Grupo;
import com.garcia.algafoodapi.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/v1/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController implements GrupoControllerOpenApi {

  @Autowired private CadastroGrupoService cadastroGrupoService;

  @Autowired private GrupoModelAssembler grupoModelAssembler;

  @Autowired private GrupoInputDisassembler grupoInputDisassembler;

  @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
  @Override
  @GetMapping
  public CollectionModel<GrupoModel> listar() {
    return grupoModelAssembler.toCollectionModel(cadastroGrupoService.listar());
  }

  @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
  @Override
  @GetMapping("/{grupoId}")
  public GrupoModel buscar(@PathVariable Long grupoId) {
    return grupoModelAssembler.toModel(cadastroGrupoService.buscarOuFalhar(grupoId));
  }

  @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public GrupoModel save(@RequestBody @Valid GrupoInput grupoInput) {
    Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);
    return grupoModelAssembler.toModel(cadastroGrupoService.save(grupo));
  }

  @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
  @Override
  @PutMapping("/{grupoId}")
  public GrupoModel atualizar(
      @PathVariable Long grupoId, @RequestBody @Valid GrupoInput grupoInput) {
    Grupo grupo = cadastroGrupoService.buscarOuFalhar(grupoId);
    grupoInputDisassembler.copyToDomainObject(grupoInput, grupo);
    return grupoModelAssembler.toModel(cadastroGrupoService.save(grupo));
  }

  @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
  @Override
  @DeleteMapping("/{grupoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void excluir(@PathVariable Long grupoId) {
    cadastroGrupoService.excluir(grupoId);
  }
}
