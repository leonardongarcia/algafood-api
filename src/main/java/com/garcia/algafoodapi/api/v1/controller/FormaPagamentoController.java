package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.assembler.FormaPagamentoModelAssembler;
import com.garcia.algafoodapi.api.v1.assembler.disassembler.FormaPagamentoInputDisassembler;
import com.garcia.algafoodapi.api.v1.model.FormaPagamentoModel;
import com.garcia.algafoodapi.api.v1.model.input.FormaPagamentoInput;
import com.garcia.algafoodapi.api.v1.openapi.controller.FormaPagamentoControllerOpenApi;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.model.FormaPagamento;
import com.garcia.algafoodapi.domain.repository.FormaPagamentoRepository;
import com.garcia.algafoodapi.domain.service.CadastroFormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path = "/v1/formas-pagamento", produces = MediaType.APPLICATION_JSON_VALUE)
public class FormaPagamentoController implements FormaPagamentoControllerOpenApi {

  @Autowired private FormaPagamentoModelAssembler formaPagamentoModelAssembler;

  @Autowired private CadastroFormaPagamentoService cadastroFormaPagamentoService;

  @Autowired private FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;

  @Autowired private FormaPagamentoRepository formaPagamentoRepository;

  @CheckSecurity.FormasPagamento.PodeConsultar
  @Override
  @GetMapping
  public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(ServletWebRequest request) {
    ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

    String etag = "0";

    OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacao();

    if (dataUltimaAtualizacao != null) {
      etag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
    }

    if (request.checkNotModified(etag)) {
      return null;
    }

    List<FormaPagamento> formasPagamento = cadastroFormaPagamentoService.listar();
    CollectionModel<FormaPagamentoModel> formasPagamentoModel =
        formaPagamentoModelAssembler.toCollectionModel(formasPagamento);

    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
        .eTag(etag)
        .body(formasPagamentoModel);
  }

  @CheckSecurity.FormasPagamento.PodeConsultar
  @Override
  @GetMapping("/{formaPagamentoId}")
  public ResponseEntity<FormaPagamentoModel> buscar(
      @PathVariable Long formaPagamentoId, ServletWebRequest request) {

    ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

    String eTag = "0";

    OffsetDateTime dataAtualizacao =
        formaPagamentoRepository.getDataAtualizacaoById(formaPagamentoId);

    if (dataAtualizacao != null) {
      eTag = String.valueOf(dataAtualizacao.toEpochSecond());
    }

    if (request.checkNotModified(eTag)) {
      return null;
    }

    FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);

    FormaPagamentoModel formaPagamentoModel = formaPagamentoModelAssembler.toModel(formaPagamento);

    return ResponseEntity.ok()
        .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
        .eTag(eTag)
        .body(formaPagamentoModel);
  }

  @CheckSecurity.FormasPagamento.PodeEditar
  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public FormaPagamentoModel save(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
    FormaPagamento formaPagamento =
        formaPagamentoInputDisassembler.toObjectModel(formaPagamentoInput);
    return formaPagamentoModelAssembler.toModel(cadastroFormaPagamentoService.save(formaPagamento));
  }

  @CheckSecurity.FormasPagamento.PodeEditar
  @Override
  @PutMapping("/{formaPagamentoId}")
  public FormaPagamentoModel alterar(
      @PathVariable Long formaPagamentoId,
      @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
    FormaPagamento formaPagamentoAtual =
        cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);

    formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);

    return formaPagamentoModelAssembler.toModel(
        cadastroFormaPagamentoService.save(formaPagamentoAtual));
  }

  @CheckSecurity.FormasPagamento.PodeEditar
  @Override
  @DeleteMapping("/{formaPagamentoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void excluir(@PathVariable Long formaPagamentoId) {
    cadastroFormaPagamentoService.excluir(formaPagamentoId);
  }
}
