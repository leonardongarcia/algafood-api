package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.AlgaLinks;
import com.garcia.algafoodapi.api.v1.assembler.ProdutoModelAssembler;
import com.garcia.algafoodapi.api.v1.assembler.disassembler.ProdutoInputDisassembler;
import com.garcia.algafoodapi.api.v1.model.ProdutoModel;
import com.garcia.algafoodapi.api.v1.model.input.ProdutoInput;
import com.garcia.algafoodapi.api.v1.openapi.controller.RestauranteProdutoControllerOpenApi;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.model.Produto;
import com.garcia.algafoodapi.domain.model.Restaurante;
import com.garcia.algafoodapi.domain.repository.ProdutoRepository;
import com.garcia.algafoodapi.domain.service.CadastroProdutoService;
import com.garcia.algafoodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/restaurantes/{restauranteId}/produtos", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoController implements RestauranteProdutoControllerOpenApi {

  @Autowired private CadastroRestauranteService cadastroRestauranteService;

  @Autowired private CadastroProdutoService cadastroProdutoService;

  @Autowired private ProdutoModelAssembler produtoModelAssembler;

  @Autowired private ProdutoInputDisassembler produtoInputDisassembler;

  @Autowired private ProdutoRepository produtoRepository;

  @Autowired
  private AlgaLinks algaLinks;

  @CheckSecurity.Restaurantes.PodeConsultar
  @Override
  @GetMapping
  public CollectionModel<ProdutoModel> listar(@PathVariable Long restauranteId,
                                              @RequestParam(required = false, defaultValue = "false") Boolean incluirInativos) {
    Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);

    List<Produto> todosProdutos = null;

    if (incluirInativos) {
      todosProdutos = produtoRepository.findTodosByRestaurante(restaurante);
    } else {
      todosProdutos = produtoRepository.findAtivosByRestaurante(restaurante);
    }

    return produtoModelAssembler.toCollectionModel(todosProdutos)
            .add(algaLinks.linkToProdutos(restauranteId));
  }

  @CheckSecurity.Restaurantes.PodeConsultar
  @Override
  @GetMapping("/{produtoId}")
  public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
    Produto produto = cadastroProdutoService.buscarOuFalhar(produtoId, restauranteId);
    return produtoModelAssembler.toModel(produto);
  }

  @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ProdutoModel adicionar(
          @PathVariable Long restauranteId, @RequestBody @Valid ProdutoInput produtoInput) {

    Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);

    Produto produto = produtoInputDisassembler.toDomainObject(produtoInput);
    produto.setRestaurante(restaurante);
    return produtoModelAssembler.toModel(cadastroProdutoService.save(produto));
  }

  @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
  @Override
  @PutMapping("/{produtoId}")
  public ProdutoModel atualizar(
          @PathVariable Long restauranteId,
          @PathVariable Long produtoId,
          @RequestBody ProdutoInput produtoInput) {

    Produto produto = cadastroProdutoService.buscarOuFalhar(produtoId, restauranteId);

    produtoInputDisassembler.copyToDomainObject(produtoInput, produto);
    return produtoModelAssembler.toModel(cadastroProdutoService.save(produto));
  }
}
