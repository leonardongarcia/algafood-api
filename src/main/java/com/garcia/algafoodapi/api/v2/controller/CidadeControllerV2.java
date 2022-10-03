package com.garcia.algafoodapi.api.v2.controller;

import com.garcia.algafoodapi.api.v2.assembler.CidadeInputDisassemblerV2;
import com.garcia.algafoodapi.api.v2.assembler.CidadeModelAssemblerV2;
import com.garcia.algafoodapi.api.v2.model.CidadeModelV2;
import com.garcia.algafoodapi.api.v2.model.input.CidadeInputV2;
import com.garcia.algafoodapi.api.v2.openapi.controller.CidadeControllerV2OpenApi;
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
@RequestMapping(path = "/v2/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeControllerV2 implements CidadeControllerV2OpenApi {

  @Autowired private CadastroCidadeService cadastroCidadeService;

  @Autowired private CidadeModelAssemblerV2 cidadeModelAssemblerV2;

  @Autowired private CidadeInputDisassemblerV2 cidadeInputDisassemblerV2;

  @Override
  @GetMapping
  public CollectionModel<CidadeModelV2> listar() {
    return cidadeModelAssemblerV2.toCollectionModel(cadastroCidadeService.listar());
  }

  @Override
  @GetMapping("/{cidadeId}")
  public CidadeModelV2 buscar(@PathVariable Long cidadeId) {
    Cidade cidade = cadastroCidadeService.buscarOuFalhar(cidadeId);

    return cidadeModelAssemblerV2.toModel(cidade);
  }

  @Override
  @Transactional
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CidadeModelV2 adicionar(@RequestBody @Valid CidadeInputV2 cidadeInputV2) {
    try {
      Cidade cidade = cidadeInputDisassemblerV2.toDomainObject(cidadeInputV2);

      return cidadeModelAssemblerV2.toModel(cadastroCidadeService.salvar(cidade));
    } catch (EstadoNaoEncontradoException e) {
      throw new NegocioException(e.getMessage(), e);
    }
  }

  @Override
  @Transactional
  @PutMapping("/{cidadeId}")
  public CidadeModelV2 atualizar(
          @PathVariable Long cidadeId, @RequestBody @Valid CidadeInputV2 cidadeInputV2) {
    try {
      Cidade cidadeAtual = cadastroCidadeService.buscarOuFalhar(cidadeId);

      cidadeInputDisassemblerV2.copyToDomainObject(cidadeInputV2, cidadeAtual);

      return cidadeModelAssemblerV2.toModel(cadastroCidadeService.salvar(cidadeAtual));
    } catch (EstadoNaoEncontradoException e) {
      throw new NegocioException(e.getMessage(), e);
    }
  }

  @Transactional
  @DeleteMapping("/{cidadeId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void excluir(@PathVariable Long cidadeId) {
    cadastroCidadeService.excluir(cidadeId);
  }
}
