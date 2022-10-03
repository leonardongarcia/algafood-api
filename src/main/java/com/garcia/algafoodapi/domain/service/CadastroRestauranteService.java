package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.exception.NegocioException;
import com.garcia.algafoodapi.domain.exception.RestauranteNaoEncontradoException;
import com.garcia.algafoodapi.domain.model.*;
import com.garcia.algafoodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CadastroRestauranteService {

  public static final String MSG_COZINHA_NAO_ENCONTRADA =
      "Não existe cadastro de cozinha com o código %d";

  @Autowired private RestauranteRepository restauranteRepository;

  @Autowired private CadastroCidadeService cadastroCidadeService;

  @Autowired private CadastroCozinhaService cadastroCozinhaService;

  @Autowired private CadastroFormaPagamentoService cadastroFormaPagamentoService;

  @Autowired private CadastroUsuarioService cadastroUsuarioService;

  public List<Restaurante> listar() {
    return restauranteRepository.findAll();
  }

  @Transactional
  public Restaurante salvar(Restaurante restaurante) {
    Long cozinhaId = restaurante.getCozinha().getId();
    Long cidadeid = restaurante.getEndereco().getCidade().getId();

    Cozinha cozinha = cadastroCozinhaService.buscarOuFalhar(cozinhaId);
    Cidade cidade = cadastroCidadeService.buscarOuFalhar(cidadeid);

    restaurante.setCozinha(cozinha);
    restaurante.getEndereco().setCidade(cidade);

    return restauranteRepository.save(restaurante);
  }

  @Transactional
  public void ativar(Long restauranteId) {
    Restaurante restauranteAtual = buscarOuFalhar(restauranteId);
    restauranteAtual.ativar();
  }

  @Transactional
  public void inativar(Long restauranteId) {
    Restaurante restauranteAtual = buscarOuFalhar(restauranteId);

    restauranteAtual.inativar();
  }

  @Transactional
  public void ativar(List<Long> restaurantesId) {
    try {
      restaurantesId.forEach(this::ativar);
    } catch (RestauranteNaoEncontradoException e) {
      throw new NegocioException(e.getMessage(), e);
    }
  }

  @Transactional
  public void inativar(List<Long> restaurantesId) {
    try {
      restaurantesId.forEach(this::inativar);
    } catch (RestauranteNaoEncontradoException e) {
      throw new NegocioException(e.getMessage(), e);
    }
  }

  public Restaurante buscarOuFalhar(Long restauranteId) {
    return restauranteRepository
        .findById(restauranteId)
        .orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
  }

  @Transactional
  public void dissociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
    Restaurante restaurante = buscarOuFalhar(restauranteId);

    FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);

    restaurante.dissociarFormaPagamento(formaPagamento);
  }

  @Transactional
  public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
    Restaurante restaurante = buscarOuFalhar(restauranteId);

    FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscarOuFalhar(formaPagamentoId);

    restaurante.associarFormaPagamento(formaPagamento);
  }

  @Transactional
  public void associarUsuario(Long restauranteId, Long usuarioId) {
    Restaurante restaurante = buscarOuFalhar(restauranteId);
    Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);
    restaurante.associarUsuario(usuario);
  }

  @Transactional
  public void dissociarUsuario(Long restauranteId, Long usuarioId) {
    Restaurante restaurante = buscarOuFalhar(restauranteId);
    Usuario usuario = cadastroUsuarioService.buscarOuFalhar(usuarioId);
    restaurante.dissociarUsuario(usuario);
  }

  @Transactional
  public void abrir(Long restauranteId) {
    Restaurante restaurante = buscarOuFalhar(restauranteId);
    restaurante.abrir();
  }

  @Transactional
  public void fechar(Long restauranteId) {
    Restaurante restaurante = buscarOuFalhar(restauranteId);
    restaurante.fechar();
  }
}
