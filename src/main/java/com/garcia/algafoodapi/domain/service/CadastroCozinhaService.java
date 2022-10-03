package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.exception.CozinhaNaoEncontradaException;
import com.garcia.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.garcia.algafoodapi.domain.model.Cozinha;
import com.garcia.algafoodapi.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
public class CadastroCozinhaService {

  public static final String MSG_COZINHA_EM_USO =
      "Cozinha com id %d não pode ser removida, pois está em uso";

  @Autowired private CozinhaRepository cozinhaRepository;

  public Page<Cozinha> listar(Pageable pageable) {
    return cozinhaRepository.findAll(pageable);
  }

  public Optional<Cozinha> buscar(@PathVariable Long cozinhaId) {
    return cozinhaRepository.findById(cozinhaId);
  }

  @Transactional
  public Cozinha salvar(Cozinha cozinha) {
    return cozinhaRepository.save(cozinha);
  }

  @Transactional
  public void excluir(Long cozinhaId) {
    try {
      cozinhaRepository.deleteById(cozinhaId);
      cozinhaRepository.flush();

    } catch (EmptyResultDataAccessException e) {
      throw new CozinhaNaoEncontradaException(cozinhaId);

    } catch (DataIntegrityViolationException e) {
      throw new EntidadeEmUsoException(String.format(MSG_COZINHA_EM_USO, cozinhaId));
    }
  }

  public Cozinha buscarOuFalhar(Long cozinhaId) {
    return cozinhaRepository
        .findById(cozinhaId)
        .orElseThrow(() -> new CozinhaNaoEncontradaException(cozinhaId));
  }
}
