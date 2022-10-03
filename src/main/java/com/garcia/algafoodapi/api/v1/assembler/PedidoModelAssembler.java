package com.garcia.algafoodapi.api.v1.assembler;

import com.garcia.algafoodapi.api.v1.AlgaLinks;
import com.garcia.algafoodapi.api.v1.controller.PedidoController;
import com.garcia.algafoodapi.api.v1.model.PedidoModel;
import com.garcia.algafoodapi.core.security.AlgaSecurity;
import com.garcia.algafoodapi.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {

  @Autowired private ModelMapper modelMapper;

  @Autowired private AlgaLinks algaLinks;

  @Autowired private AlgaSecurity algaSecurity;

  public PedidoModelAssembler() {
    super(PedidoController.class, PedidoModel.class);
  }

  @Override
  public PedidoModel toModel(Pedido pedido) {
    PedidoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
    modelMapper.map(pedido, pedidoModel);

    // Não usei o método algaSecurity.podePesquisarPedidos(clienteId, restauranteId) aqui,
    // porque na geração do link, não temos o id do cliente e do restaurante,
    // então precisamos saber apenas se a requisição está autenticada e tem o escopo de leitura
    if (algaSecurity.podePesquisarPedidos()) {
      pedidoModel.add(algaLinks.linkToPedidos("pedidos"));
    }

    if (algaSecurity.podeGerenciarPedidos(pedido.getCodigo())) {
      if (pedido.podeSerConfirmado()) {
        pedidoModel.add(algaLinks.linkToConfirmacaoPedido(pedido.getCodigo(), "confirmar"));
      }

      if (pedido.podeSerCancelado()) {
        pedidoModel.add(algaLinks.linkToCancelamentoPedido(pedido.getCodigo(), "cancelar"));
      }

      if (pedido.podeSerEntregue()) {
        pedidoModel.add(algaLinks.linkToEntregaPedido(pedido.getCodigo(), "entregar"));
      }
    }

    if (algaSecurity.podeConsultarRestaurantes()) {
      pedidoModel
          .getRestaurante()
          .add(algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));
    }

    if (algaSecurity.podeConsultarUsuariosGruposPermissoes()) {
      pedidoModel.getCliente().add(algaLinks.linkToUsuario(pedido.getCliente().getId()));
    }

    if (algaSecurity.podeConsultarFormasPagamento()) {
      pedidoModel
          .getFormaPagamento()
          .add(algaLinks.linkToFormaPagamento(pedido.getFormaPagamento().getId()));
    }

    if (algaSecurity.podeConsultarCidades()) {
      pedidoModel
          .getEnderecoEntrega()
          .getCidade()
          .add(algaLinks.linkToCidade(pedido.getEnderecoEntrega().getCidade().getId()));
    }

    // Quem pode consultar restaurantes, também pode consultar os produtos dos restaurantes
    if (algaSecurity.podeConsultarRestaurantes()) {
      pedidoModel
          .getItens()
          .forEach(
              item -> {
                item.add(
                    algaLinks.linkToProduto(
                        pedidoModel.getRestaurante().getId(), item.getProdutoId(), "produto"));
              });
    }

    return pedidoModel;
  }

  //  public PedidoModel toModel(Pedido pedido) {
  //    PedidoModel pedidoModel = createModelWithId(pedido.getCodigo(), pedido);
  //    modelMapper.map(pedido, pedidoModel);
  //
  //    pedidoModel.add(algaLinks.linkToPedidos());
  //
  //
  // pedidoModel.getRestaurante().add(algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));
  ////    pedidoModel.getRestaurante()
  ////
  // .add(linkTo(methodOn(RestauranteController.class).buscar(pedido.getRestaurante().getId()))
  ////                .withSelfRel());
  //    pedidoModel.getCliente().add(algaLinks.linkToUsuario(pedido.getCliente().getId()));
  //
  //
  // pedidoModel.getFormaPagamento().add(algaLinks.linkToFormaPagamento(pedido.getFormaPagamento().getId()));
  //
  //    pedidoModel.getEnderecoEntrega().getCidade()
  //
  // .add(linkTo(methodOn(CidadeController.class).buscar(pedidoModel.getEnderecoEntrega().getCidade().getId()))
  //                    .withSelfRel());
  //
  //
  //    pedidoModel.getItens().forEach(item -> {
  //
  // item.add(linkTo(methodOn(RestauranteProdutoController.class).buscar(pedidoModel.getRestaurante().getId(),
  //              item.getProdutoId())).withSelfRel());
  //            });
  //    return pedidoModel;
  //  }

  public List<PedidoModel> toCollectionModel(Collection<Pedido> pedidos) {
    return pedidos.stream().map(this::toModel).collect(Collectors.toList());
  }
}
