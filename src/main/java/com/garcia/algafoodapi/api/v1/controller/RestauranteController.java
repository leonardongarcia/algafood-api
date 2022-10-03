package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.assembler.RestauranteApenasNomeModelAssembler;
import com.garcia.algafoodapi.api.v1.assembler.RestauranteBasicoModelAssembler;
import com.garcia.algafoodapi.api.v1.assembler.RestauranteModelAssembler;
import com.garcia.algafoodapi.api.v1.assembler.disassembler.RestauranteInputDisassembler;
import com.garcia.algafoodapi.api.v1.model.RestauranteApenasNomeModel;
import com.garcia.algafoodapi.api.v1.model.RestauranteBasicoModel;
import com.garcia.algafoodapi.api.v1.model.RestauranteModel;
import com.garcia.algafoodapi.api.v1.model.input.RestauranteInput;
import com.garcia.algafoodapi.api.v1.openapi.controller.RestauranteControllerOpenApi;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.exception.CidadeNaoEncontradaException;
import com.garcia.algafoodapi.domain.exception.CozinhaNaoEncontradaException;
import com.garcia.algafoodapi.domain.exception.NegocioException;
import com.garcia.algafoodapi.domain.model.Restaurante;
import com.garcia.algafoodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/restaurantes", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteController implements RestauranteControllerOpenApi {

  @Autowired private CadastroRestauranteService cadastroRestauranteService;

  @Autowired private RestauranteModelAssembler restauranteModelAssembler;

  @Autowired private RestauranteInputDisassembler restauranteInputDisassembler;

  @Autowired private RestauranteBasicoModelAssembler restauranteBasicoModelAssembler;

  @Autowired private RestauranteApenasNomeModelAssembler restauranteApenasNomeModelAssembler;

  //  @ApiOperation(value = "Lista restaurantes", response = RestauranteBasicoModelOpenApi.class)
  //  @ApiImplicitParam(value = {
  //          @ApiImplicitParam(value = "Nome da projeção de pedidos", allowableValues =
  // "apenas-nome",
  //                  name = "projecao", paramType = "query", dataTypeClass = String.class)
  //  })

  @CheckSecurity.Restaurantes.PodeConsultar
  @Override
  //  @JsonView(RestauranteView.Resumo.class)
  @GetMapping
  public CollectionModel<RestauranteBasicoModel> listar() {
    return restauranteBasicoModelAssembler.toCollectionModel(cadastroRestauranteService.listar());
  }

  @CheckSecurity.Restaurantes.PodeConsultar
  @Override
  //  @JsonView(RestauranteView.ApenasNome.class)
  @GetMapping(params = "projecao=apenas-nome")
  public CollectionModel<RestauranteApenasNomeModel> listarApenasNomes() {
    return restauranteApenasNomeModelAssembler.toCollectionModel(
        cadastroRestauranteService.listar());
  }

  @CheckSecurity.Restaurantes.PodeConsultar
  @Override
  @GetMapping("/{restauranteId}")
  public RestauranteModel buscar(@PathVariable Long restauranteId) {
    Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);

    return restauranteModelAssembler.toModel(restaurante);
  }

  @CheckSecurity.Restaurantes.PodeGerenciarCadastro
  @Override
  @Transactional
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
    try {
      Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);

      return restauranteModelAssembler.toModel(cadastroRestauranteService.salvar(restaurante));
    } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
      throw new NegocioException(e.getMessage(), e);
    }
  }

  @CheckSecurity.Restaurantes.PodeGerenciarCadastro
  @Override
  @Transactional
  @PutMapping("/{restauranteId}")
  public RestauranteModel atualizar(
      @PathVariable Long restauranteId, @RequestBody @Valid RestauranteInput restauranteInput) {
    try {
      Restaurante restauranteAtual = cadastroRestauranteService.buscarOuFalhar(restauranteId);

      restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);

      return restauranteModelAssembler.toModel(cadastroRestauranteService.salvar(restauranteAtual));
    } catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
      throw new NegocioException(e.getMessage(), e);
    }
  }

  @CheckSecurity.Restaurantes.PodeGerenciarCadastro
  @Override
  @PutMapping("/{restauranteId}/ativo")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> ativar(@PathVariable Long restauranteId) {
    cadastroRestauranteService.ativar(restauranteId);
    return ResponseEntity.noContent().build();
  }

  @CheckSecurity.Restaurantes.PodeGerenciarCadastro
  @Override
  @DeleteMapping("/{restauranteId}/ativo")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> inativar(@PathVariable Long restauranteId) {
    cadastroRestauranteService.inativar(restauranteId);
    return ResponseEntity.noContent().build();
  }

  @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
  @Override
  @PutMapping("/{restauranteId}/fechamento")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void>  fechar(@PathVariable Long restauranteId) {
    cadastroRestauranteService.fechar(restauranteId);
    return ResponseEntity.noContent().build();
  }

  @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
  @Override
  @PutMapping("/{restauranteId}/abertura")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void>  abrir(@PathVariable Long restauranteId) {
    cadastroRestauranteService.abrir(restauranteId);
    return ResponseEntity.noContent().build();
  }

  @CheckSecurity.Restaurantes.PodeGerenciarCadastro
  @Override
  @PutMapping("/ativacoes{restaurantesId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void ativarMultiplos(@RequestBody List<Long> restaurantesId) {
    cadastroRestauranteService.ativar(restaurantesId);
  }

  @CheckSecurity.Restaurantes.PodeGerenciarCadastro
  @Override
  @DeleteMapping("/ativacoes{restaurantesId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void inativarMultiplos(@RequestBody List<Long> restaurantesId) {
    cadastroRestauranteService.inativar(restaurantesId);
  }
}
