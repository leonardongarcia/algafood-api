package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.assembler.PedidoModelAssembler;
import com.garcia.algafoodapi.api.v1.assembler.PedidoResumoModelAssembler;
import com.garcia.algafoodapi.api.v1.assembler.disassembler.PedidoInputDisassembler;
import com.garcia.algafoodapi.api.v1.model.PedidoModel;
import com.garcia.algafoodapi.api.v1.model.PedidoResumoModel;
import com.garcia.algafoodapi.api.v1.model.input.PedidoInput;
import com.garcia.algafoodapi.api.v1.openapi.controller.PedidoControllerOpenApi;
import com.garcia.algafoodapi.core.data.PageWrapper;
import com.garcia.algafoodapi.core.data.PageableTranslator;
import com.garcia.algafoodapi.core.security.AlgaSecurity;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.garcia.algafoodapi.domain.exception.NegocioException;
import com.garcia.algafoodapi.domain.filter.PedidoFilter;
import com.garcia.algafoodapi.domain.model.Pedido;
import com.garcia.algafoodapi.domain.model.Usuario;
import com.garcia.algafoodapi.domain.service.EmissaoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "/v1/pedidos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PedidoController implements PedidoControllerOpenApi {

  @Autowired private EmissaoPedidoService emissaoPedidoService;

  @Autowired private PedidoModelAssembler pedidoModelAssembler;

  @Autowired private PedidoResumoModelAssembler pedidoResumoModelAssembler;

  @Autowired private PedidoInputDisassembler pedidoInputDisassembler;

  @Autowired private PagedResourcesAssembler pagedResourcesAssembler;

  @Autowired private AlgaSecurity algaSecurity;

  @CheckSecurity.Pedidos.PodePesquisar
  @Override
  @GetMapping
  public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter pedidoFilter, Pageable pageable) {
    Pageable pageableTraduzido = traduzirPageable(pageable);

    Page<Pedido> pedidosPage = emissaoPedidoService.listarTodos(pedidoFilter, pageableTraduzido);

    pedidosPage = new PageWrapper<>(pedidosPage, pageable);

    return pagedResourcesAssembler.toModel(pedidosPage, pedidoResumoModelAssembler);
  }

  @CheckSecurity.Pedidos.PodeBuscar
  @Override
  @GetMapping("/{codigoPedido}")
  public PedidoModel buscar(@PathVariable String codigoPedido) {
    return pedidoModelAssembler.toModel(emissaoPedidoService.buscarOuFalhar(codigoPedido));
  }

  @CheckSecurity.Pedidos.PodeCriar
  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
    try {
      Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

      novoPedido.setCliente(new Usuario());
      novoPedido.getCliente().setId(algaSecurity.getUsuarioId());

      novoPedido = emissaoPedidoService.emitir(novoPedido);
      return pedidoModelAssembler.toModel(novoPedido);
    } catch (EntidadeNaoEncontradaException e) {
      throw new NegocioException(e.getMessage(), e);
    }
  }

  private Pageable traduzirPageable(Pageable apiPageable) {
    var mapeamento =
        Map.of(
            "codigo", "codigo",
            "subtotal", "subtotal",
            "taxaFrete", "taxaFrete",
            "valorTotal", "valorTotal",
            "dataCriacao", "dataCriacao",
            "nomerestaurante", "restaurante.nome",
            "restaurante.id", "restaurante.id",
            "cliente.id", "cliente.id",
            "cliente.nome", "cliente.nome");
    return PageableTranslator.translate(apiPageable, mapeamento);
  }
}
