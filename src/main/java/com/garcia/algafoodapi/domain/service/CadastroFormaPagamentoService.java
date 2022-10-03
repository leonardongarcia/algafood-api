package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.garcia.algafoodapi.domain.exception.FormaPagamentoNaoEncontradaException;
import com.garcia.algafoodapi.domain.model.FormaPagamento;
import com.garcia.algafoodapi.domain.repository.FormaPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CadastroFormaPagamentoService {

  public static final String MSG_FORMAPAGAMENTO_EM_USO =
      "A forma de pagamento de código %d não pode ser excluída" + ", pois está em uso!";
  @Autowired private FormaPagamentoRepository formaPagamentoRepository;

  public List<FormaPagamento> listar() {
    return formaPagamentoRepository.findAll();
  }

  public FormaPagamento buscarOuFalhar(Long formaPagamentoId) {
    return formaPagamentoRepository
        .findById(formaPagamentoId)
        .orElseThrow(() -> new FormaPagamentoNaoEncontradaException(formaPagamentoId));
  }

  @Transactional
  public FormaPagamento save(FormaPagamento formaPagamento) {
    return formaPagamentoRepository.save(formaPagamento);
  }

  @Transactional
  public void excluir(Long formaPagamentoId) {
    try {
      formaPagamentoRepository.deleteById(formaPagamentoId);
      formaPagamentoRepository.flush();
    } catch (DataIntegrityViolationException ex) {
      throw new EntidadeEmUsoException(String.format(MSG_FORMAPAGAMENTO_EM_USO, formaPagamentoId));
    } catch (EmptyResultDataAccessException ex) {
      throw new FormaPagamentoNaoEncontradaException(formaPagamentoId);
    }
  }
}
