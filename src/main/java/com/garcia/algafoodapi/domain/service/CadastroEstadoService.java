package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.garcia.algafoodapi.domain.exception.EstadoNaoEncontradoException;
import com.garcia.algafoodapi.domain.model.Estado;
import com.garcia.algafoodapi.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroEstadoService {

  public static final String MSG_ESTADO_EM_USO =
      "Estado de id %d não pode ser removido, pois está em uso";

  @Autowired private EstadoRepository estadoRepository;

  public List<Estado> listar() {
    return estadoRepository.findAll();
  }

  public Optional<Estado> buscar(Long estadoId) {
    return estadoRepository.findById(estadoId);
  }

  @Transactional
  public Estado salvar(Estado estado) {
    return estadoRepository.save(estado);
  }

  @Transactional
  public void excluir(Long estadoId) {
    try {
      estadoRepository.deleteById(estadoId);
      estadoRepository.flush();

    } catch (DataIntegrityViolationException e) {
      throw new EntidadeEmUsoException(String.format(MSG_ESTADO_EM_USO, estadoId));

    } catch (EmptyResultDataAccessException e) {
      throw new EstadoNaoEncontradoException(estadoId);
    }
  }

  public Estado buscarOuFalhar(Long estadoId) {
    return estadoRepository
        .findById(estadoId)
        .orElseThrow(() -> new EstadoNaoEncontradoException(estadoId));
  }
}
