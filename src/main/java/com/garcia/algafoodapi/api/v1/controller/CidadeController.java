package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.assembler.CidadeModelAssembler;
import com.garcia.algafoodapi.api.v1.assembler.disassembler.CidadeInputDisassembler;
import com.garcia.algafoodapi.api.v1.model.CidadeModel;
import com.garcia.algafoodapi.api.v1.model.input.CidadeInput;
import com.garcia.algafoodapi.api.v1.openapi.controller.CidadeControllerOpenApi;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.exception.EstadoNaoEncontradoException;
import com.garcia.algafoodapi.domain.exception.NegocioException;
import com.garcia.algafoodapi.domain.model.Cidade;
import com.garcia.algafoodapi.domain.service.CadastroCidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/v1/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeController implements CidadeControllerOpenApi {

  @Autowired private CadastroCidadeService cadastroCidadeService;

  @Autowired private CidadeModelAssembler cidadeModelAssembler;

  @Autowired private CidadeInputDisassembler cidadeInputDisassembler;

  @CheckSecurity.Cidades.PodeConsultar
  @Override
  @GetMapping
  public CollectionModel<CidadeModel> listar() {
    return cidadeModelAssembler.toCollectionModel(cadastroCidadeService.listar());
  }

  @CheckSecurity.Cidades.PodeConsultar
  @Override
  @GetMapping("/{cidadeId}")
  public CidadeModel buscar(@PathVariable Long cidadeId) {
    Cidade cidade = cadastroCidadeService.buscarOuFalhar(cidadeId);

    return cidadeModelAssembler.toModel(cidade);
  }

  @CheckSecurity.Cidades.PodeEditar
  @Override
  @Transactional
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
    try {
      Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);

      return cidadeModelAssembler.toModel(cadastroCidadeService.salvar(cidade));
    } catch (EstadoNaoEncontradoException e) {
      throw new NegocioException(e.getMessage(), e);
    }
  }

  @CheckSecurity.Cidades.PodeEditar
  @Override
  @Transactional
  @PutMapping("/{cidadeId}")
  public CidadeModel atualizar(
      @PathVariable Long cidadeId, @RequestBody @Valid CidadeInput cidadeInput) {
    try {
      Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(cidadeId);

      cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);

      return cidadeModelAssembler.toModel(cadastroCidadeService.salvar(cidadeAtual));
    } catch (EstadoNaoEncontradoException e) {
      throw new NegocioException(e.getMessage(), e);
    }
  }

  @CheckSecurity.Cidades.PodeEditar
  @Override
  @Transactional
  @DeleteMapping("/{cidadeId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void excluir(@PathVariable Long cidadeId) {
    cadastroCidadeService.excluir(cidadeId);
  }
}
