package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.assembler.CozinhaModelAssembler;
import com.garcia.algafoodapi.api.v1.assembler.disassembler.CozinhaInputDisassembler;
import com.garcia.algafoodapi.api.v1.model.CozinhaModel;
import com.garcia.algafoodapi.api.v1.model.input.CozinhaInput;
import com.garcia.algafoodapi.api.v1.openapi.controller.CozinhaControllerOpenApi;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.model.Cozinha;
import com.garcia.algafoodapi.domain.service.CadastroCozinhaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/v1/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CozinhaController implements CozinhaControllerOpenApi {

  @Autowired private CadastroCozinhaService cadastroCozinhaService;

  @Autowired private CozinhaModelAssembler cozinhaModelAssembler;

  @Autowired private CozinhaInputDisassembler cozinhaInputDisassembler;

  @Autowired private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;

  @CheckSecurity.Cozinhas.PodeConsultar
  @Override
  @GetMapping
  public PagedModel<CozinhaModel> listar(@PageableDefault(size = 10) Pageable pageable) {
    System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());

    log.info("Consultando cozinhas com páginas de {} registros", pageable.getPageSize());
    Page<Cozinha> cozinhasPage = cadastroCozinhaService.listar(pageable);

    return pagedResourcesAssembler.toModel(cozinhasPage, cozinhaModelAssembler);
  }

  @CheckSecurity.Cozinhas.PodeConsultar
  @Override
  @GetMapping("/{cozinhaId}")
  public CozinhaModel buscar(@PathVariable Long cozinhaId) {
    return cozinhaModelAssembler.toModel(cadastroCozinhaService.buscarOuFalhar(cozinhaId));
  }

  @CheckSecurity.Cozinhas.PodeEditar
  @Override
  @Transactional
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
    Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
    return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinha));
  }

  @CheckSecurity.Cozinhas.PodeEditar
  @Override
  @Transactional
  @PutMapping("/{cozinhaId}")
  public CozinhaModel atualizar(
      @PathVariable Long cozinhaId, @RequestBody @Valid CozinhaInput cozinhaInput) {
    Cozinha cozinhaAtual = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
    cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);

    return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinhaAtual));
  }

  @CheckSecurity.Cozinhas.PodeEditar
  @Override
  @DeleteMapping("/{cozinhaId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void excluir(@PathVariable Long cozinhaId) {
    cadastroCozinhaService.excluir(cozinhaId);
  }
}
