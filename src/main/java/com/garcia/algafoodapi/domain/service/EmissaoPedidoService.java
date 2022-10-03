package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.exception.NegocioException;
import com.garcia.algafoodapi.domain.exception.PedidoNaoEncontradoException;
import com.garcia.algafoodapi.domain.filter.PedidoFilter;
import com.garcia.algafoodapi.domain.model.*;
import com.garcia.algafoodapi.domain.repository.PedidoRepository;
import com.garcia.algafoodapi.infrastructure.repository.spec.PedidoSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmissaoPedidoService {

  @Autowired private PedidoRepository pedidoRepository;

  @Autowired private CadastroUsuarioService cadastroUsuarioService;

  @Autowired private CadastroProdutoService cadastroProdutoService;

  @Autowired private CadastroCidadeService cadastroCidadeService;

  @Autowired private CadastroRestauranteService cadastroRestauranteService;

  @Autowired private CadastroFormaPagamentoService cadastroFormaPagamentoService;

  public Page<Pedido> listarTodos(PedidoFilter pedidoFilter, Pageable pageable) {
    return pedidoRepository.findAll(PedidoSpecs.usandoFiltro(pedidoFilter), pageable);
  }

  @Transactional
  public Pedido emitir(Pedido pedido) {
    validarPedido(pedido);
    validarItens(pedido);

    pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
    pedido.calcularValorTotal();

    return pedidoRepository.save(pedido);
  }

  private void validarPedido(Pedido pedido) {
    Cidade cidade =
        cadastroCidadeService.buscarOuFalhar(pedido.getEnderecoEntrega().getCidade().getId());
    Usuario cliente = cadastroUsuarioService.buscarOuFalhar(pedido.getCliente().getId());
    Restaurante restaurante =
        cadastroRestauranteService.buscarOuFalhar(pedido.getRestaurante().getId());
    FormaPagamento formaPagamento =
        cadastroFormaPagamentoService.buscarOuFalhar(pedido.getFormaPagamento().getId());

    pedido.getEnderecoEntrega().setCidade(cidade);
    pedido.setCliente(cliente);
    pedido.setRestaurante(restaurante);
    pedido.setFormaPagamento(formaPagamento);

    if (restaurante.naoAceitaFormaPagamento(formaPagamento)) {
      throw new NegocioException(
          String.format(
              "Forma de pagamento '%s' não é aceita por esse restaurante.",
              formaPagamento.getDescricao()));
    }
  }

  private void validarItens(Pedido pedido) {
    pedido
        .getItens()
        .forEach(
            itemPedido -> {
              Produto produto =
                  cadastroProdutoService.buscarOuFalhar(
                      pedido.getRestaurante().getId(), itemPedido.getProduto().getId());

              itemPedido.setPedido(pedido);
              itemPedido.setProduto(produto);
              itemPedido.setPrecoUnitario(produto.getPreco());
            });
  }

  public Pedido buscarOuFalhar(String codigoPedido) {
    return pedidoRepository
        .findByCodigo(codigoPedido)
        .orElseThrow(() -> new PedidoNaoEncontradoException(codigoPedido));
  }
}
