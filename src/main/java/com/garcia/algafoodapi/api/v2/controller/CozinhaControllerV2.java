package com.garcia.algafoodapi.api.v2.controller;

import com.garcia.algafoodapi.api.v2.assembler.CozinhaInputDisassemblerV2;
import com.garcia.algafoodapi.api.v2.assembler.CozinhaModelAssemblerV2;
import com.garcia.algafoodapi.api.v2.model.CozinhaModelV2;
import com.garcia.algafoodapi.api.v2.model.input.CozinhaInputV2;
import com.garcia.algafoodapi.domain.model.Cozinha;
import com.garcia.algafoodapi.domain.service.CadastroCozinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/v2/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaControllerV2
    implements com.garcia.algafoodapi.api.v2.openapi.controller.CozinhaControllerV2OpenApi {

  @Autowired private CadastroCozinhaService cadastroCozinhaService;

  @Autowired private CozinhaModelAssemblerV2 cozinhaModelAssemblerV2;

  @Autowired private CozinhaInputDisassemblerV2 cozinhaInputDisassemblerV2;

  @Autowired private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;

  @Override
  @GetMapping
  public PagedModel<CozinhaModelV2> listar(@PageableDefault(size = 10) Pageable pageable) {
    Page<Cozinha> cozinhasPage = cadastroCozinhaService.listar(pageable);

    return pagedResourcesAssembler.toModel(cozinhasPage, cozinhaModelAssemblerV2);
  }

  @Override
  @GetMapping("/{cozinhaId}")
  public CozinhaModelV2 buscar(@PathVariable Long cozinhaId) {
    return cozinhaModelAssemblerV2.toModel(cadastroCozinhaService.buscarOuFalhar(cozinhaId));
  }

  @Override
  @Transactional
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CozinhaModelV2 adicionar(@RequestBody @Valid CozinhaInputV2 cozinhaInputV2) {
    Cozinha cozinha = cozinhaInputDisassemblerV2.toDomainObject(cozinhaInputV2);
    return cozinhaModelAssemblerV2.toModel(cadastroCozinhaService.salvar(cozinha));
  }

  @Override
  @Transactional
  @PutMapping("/{cozinhaId}")
  public CozinhaModelV2 atualizar(
      @PathVariable Long cozinhaId, @RequestBody @Valid CozinhaInputV2 cozinhaInputV2) {
    Cozinha cozinhaAtual = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
    cozinhaInputDisassemblerV2.copyToDomainObject(cozinhaInputV2, cozinhaAtual);

    return cozinhaModelAssemblerV2.toModel(cadastroCozinhaService.salvar(cozinhaAtual));
  }

  @Override
  @DeleteMapping("/{cozinhaId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void excluir(@PathVariable Long cozinhaId) {
    cadastroCozinhaService.excluir(cozinhaId);
  }
}
