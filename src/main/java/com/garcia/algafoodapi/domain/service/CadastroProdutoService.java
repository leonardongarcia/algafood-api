package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.exception.ProdutoNaoEncontradoException;
import com.garcia.algafoodapi.domain.model.Produto;
import com.garcia.algafoodapi.domain.model.Restaurante;
import com.garcia.algafoodapi.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CadastroProdutoService {

  public static final String MSG_PRODUTO_NAO_ENCONTRADO =
      "Não existe um cadastro de produto com o código %d " + "para o restaurante de código %d";

  @Autowired private ProdutoRepository produtoRepository;

  @Autowired private CadastroRestauranteService cadastroRestauranteService;

  public List<Produto> listar() {
    return produtoRepository.findAll();
  }

  public Produto buscarOuFalhar(Long produtoId, Long restauranteId) {
    return produtoRepository
        .findById(produtoId, restauranteId)
        .orElseThrow(() -> new ProdutoNaoEncontradoException(produtoId, restauranteId));
  }

  public Produto buscar(Long produtoId) {
    return produtoRepository
        .findById(produtoId)
        .orElseThrow(
            () ->
                new ProdutoNaoEncontradoException(
                    String.format("Não existe um cadastro de produto com o código %d", produtoId)));
  }

  public List<Produto> porRestaurante(Restaurante restaurante) {
    return produtoRepository.findTodosByRestaurante(restaurante);
  }

  @Transactional
  public Produto save(Produto produto) {
    return produtoRepository.save(produto);
  }

  //  public void atualizar(Long restauranteId, Long produtoId) {
  //    Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);
  //    Produto produto = buscarOuFalhar(produtoId);
  //    List<Produto> produtos = restaurante.getProdutos();
  //
  //    contemProduto(restauranteId, produtoId, produto, produtos);
  //
  //    produto.setRestaurante(restaurante);
  //  }

  //  public void contemProduto(
  //      Long restauranteId, Long produtoId, Produto produto, List<Produto> produtos) {
  //    if (!produtos.contains(produto)) {
  //      throw new ProdutoNaoEncontradoException(
  //          String.format(MSG_PRODUTO_NAO_ENCONTRADO, produtoId, restauranteId));
  //    }
  //  }
}
