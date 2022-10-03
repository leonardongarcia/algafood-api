package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.assembler.UsuarioModelAssembler;
import com.garcia.algafoodapi.api.v1.assembler.disassembler.UsuarioInputDisassembler;
import com.garcia.algafoodapi.api.v1.model.UsuarioModel;
import com.garcia.algafoodapi.api.v1.model.input.SenhaInput;
import com.garcia.algafoodapi.api.v1.model.input.UsuarioComSenhaInput;
import com.garcia.algafoodapi.api.v1.model.input.UsuarioInput;
import com.garcia.algafoodapi.api.v1.openapi.controller.UsuarioControllerOpenApi;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.model.Usuario;
import com.garcia.algafoodapi.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/v1/usuarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioController implements UsuarioControllerOpenApi {

  @Autowired private CadastroUsuarioService cadastroUsuarioService;

  @Autowired private UsuarioModelAssembler usuarioModelAssembler;

  @Autowired private UsuarioInputDisassembler usuarioInputDisassembler;

  @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
  @Override
  @GetMapping
  public CollectionModel<UsuarioModel> listar() {
    return usuarioModelAssembler.toCollectionModel(cadastroUsuarioService.listar());
  }

  @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
  @Override
  @GetMapping("/{usuarioId}")
  public UsuarioModel buscar(@PathVariable Long usuarioId) {
    return usuarioModelAssembler.toModel(cadastroUsuarioService.buscarOuFalhar(usuarioId));
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UsuarioModel adicionar(@RequestBody @Valid UsuarioComSenhaInput usuarioComSenhaInput) {
    Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioComSenhaInput);
    return usuarioModelAssembler.toModel(cadastroUsuarioService.save(usuario));
  }

  @CheckSecurity.UsuariosGruposPermissoes.PodeAlterarUsuario
  @Override
  @PutMapping("/{usuarioId}")
  public UsuarioModel atualizar(
          @PathVariable Long usuarioId, @RequestBody @Valid UsuarioInput usuarioInput) {

    Usuario usuarioAtual = cadastroUsuarioService.buscarOuFalhar(usuarioId);
    usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuarioAtual);
    return usuarioModelAssembler.toModel(cadastroUsuarioService.save(usuarioAtual));
  }

  @CheckSecurity.UsuariosGruposPermissoes.PodeAlterarPropriaSenha
  @Override
  @PutMapping("/{usuarioId}/senha")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void alterarSenha(@PathVariable Long usuarioId, @RequestBody SenhaInput senhaInput) {
    cadastroUsuarioService.alterarSenha(usuarioId, senhaInput.getSenhaAtual(), senhaInput.getNovaSenha());
  }
}
