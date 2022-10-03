package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.AlgaLinks;
import com.garcia.algafoodapi.api.v1.assembler.FormaPagamentoModelAssembler;
import com.garcia.algafoodapi.api.v1.model.FormaPagamentoModel;
import com.garcia.algafoodapi.api.v1.openapi.controller.RestauranteFormaPagamentoControllerOpenApi;
import com.garcia.algafoodapi.core.security.AlgaSecurity;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.model.Restaurante;
import com.garcia.algafoodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    path = "/v1/restaurantes/{restauranteId}/formas-pagamento",
    produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteFormaPagamentoController
    implements RestauranteFormaPagamentoControllerOpenApi {

  @Autowired private CadastroRestauranteService cadastroRestauranteService;

  @Autowired private FormaPagamentoModelAssembler formaPagamentoModelAssembler;

  @Autowired private AlgaLinks algaLinks;

  @Autowired private AlgaSecurity algaSecurity;

  @CheckSecurity.Restaurantes.PodeConsultar
  @Override
  @GetMapping
  public CollectionModel<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {
    Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);

    CollectionModel<FormaPagamentoModel> formasPagamentoModel =
        formaPagamentoModelAssembler
            .toCollectionModel(restaurante.getFormasPagamento())
            .removeLinks();

    formasPagamentoModel.add(algaLinks.linkToRestauranteFormasPagamento(restauranteId));

    if (algaSecurity.podeGerenciarFuncionamentoRestaurantes(restauranteId)) {
      formasPagamentoModel.add(
          algaLinks.linkToRestauranteFormaPagamentoAssociacao(restauranteId, "associar"));

      formasPagamentoModel
          .getContent()
          .forEach(
              formaPagamentoModel -> {
                formaPagamentoModel.add(
                    algaLinks.linkToRestauranteFormaPagamentoDissociacao(
                        restauranteId, formaPagamentoModel.getId(), "dissociar"));
              });
    }

    return formasPagamentoModel;
  }

  @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
  @Override
  @DeleteMapping("/{formaPagamentoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> dissociar(
      @PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
    cadastroRestauranteService.dissociarFormaPagamento(restauranteId, formaPagamentoId);

    return ResponseEntity.noContent().build();
  }

  @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
  @Override
  @PutMapping("/{formaPagamentoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> associar(
      @PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
    cadastroRestauranteService.associarFormaPagamento(restauranteId, formaPagamentoId);
    return ResponseEntity.noContent().build();
  }
}
