package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.assembler.disassembler.EstadoInputDisassembler;
import com.garcia.algafoodapi.api.v1.assembler.EstadoModelAssembler;
import com.garcia.algafoodapi.api.v1.model.EstadoModel;
import com.garcia.algafoodapi.api.v1.model.input.EstadoInput;
import com.garcia.algafoodapi.api.v1.openapi.controller.EstadoControllerOpenApi;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.model.Estado;
import com.garcia.algafoodapi.domain.service.CadastroEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/v1/estados", produces = MediaType.APPLICATION_JSON_VALUE)
public class EstadoController implements EstadoControllerOpenApi {

  @Autowired private CadastroEstadoService cadastroEstadoService;

  @Autowired private EstadoInputDisassembler estadoInputDisassembler;

  @Autowired private EstadoModelAssembler estadoModelAssembler;

  @CheckSecurity.Cidades.PodeConsultar
  @Override
  @GetMapping
  public CollectionModel<EstadoModel> listar() {
    return estadoModelAssembler.toCollectionModel(cadastroEstadoService.listar());
  }

  @CheckSecurity.Cidades.PodeConsultar
  @Override
  @GetMapping("/{estadoId}")
  public EstadoModel buscar(@PathVariable Long estadoId) {
    return estadoModelAssembler.toModel(cadastroEstadoService.buscarOuFalhar(estadoId));
  }

  @CheckSecurity.Cidades.PodeEditar
  @Override
  @Transactional
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
    Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);
    return estadoModelAssembler.toModel(cadastroEstadoService.salvar(estado));
  }

  @CheckSecurity.Cidades.PodeEditar
  @Override
  @Transactional
  @PutMapping("/{estadoId}")
  public EstadoModel atualizar(
          @PathVariable Long estadoId, @RequestBody @Valid EstadoInput estadoInput) {
    Estado estadoAtual = cadastroEstadoService.buscarOuFalhar(estadoId);

    estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);

    return estadoModelAssembler.toModel(cadastroEstadoService.salvar(estadoAtual));
  }

  @CheckSecurity.Cidades.PodeEditar
  @Override
  @Transactional
  @DeleteMapping("/{estadoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void excluir(@PathVariable Long estadoId) {
    cadastroEstadoService.excluir(estadoId);
  }
}
