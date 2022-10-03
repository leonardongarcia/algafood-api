package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.exception.CidadeNaoEncontradaException;
import com.garcia.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.garcia.algafoodapi.domain.model.Cidade;
import com.garcia.algafoodapi.domain.model.Estado;
import com.garcia.algafoodapi.domain.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CadastroCidadeService {

  public static final String MSG_CIDADE_EM_USO =
      "Cidade com id %d não pode ser removida, pois está em uso";

  public static final String MSG_CIDADE_NAO_ENCONTRADA =
      "Não existe cadastro de cidade com o código %d";

  @Autowired private CidadeRepository cidadeRepository;

  @Autowired private CadastroEstadoService cadastroEstadoService;

  public List<Cidade> listar() {
    return cidadeRepository.findAll();
  }

  public Optional<Cidade> buscar(Long cidadeId) {
    return cidadeRepository.findById(cidadeId);
  }

  @Transactional
  public Cidade salvar(Cidade cidade) {
    Long estadoId = cidade.getEstado().getId();
    Estado estado = cadastroEstadoService.buscarOuFalhar(estadoId);

    cidade.setEstado(estado);

    return cidadeRepository.save(cidade);
  }

  @Transactional
  public void excluir(Long cidadeId) {
    try {
      cidadeRepository.deleteById(cidadeId);
      cidadeRepository.flush();
    } catch (DataIntegrityViolationException e) {
      throw new EntidadeEmUsoException(String.format(MSG_CIDADE_EM_USO, cidadeId));
    } catch (EmptyResultDataAccessException e) {
      throw new CidadeNaoEncontradaException(String.format(MSG_CIDADE_NAO_ENCONTRADA, cidadeId));
    }
  }

  public Cidade buscarOuFalhar(Long cidadeId) {
    return cidadeRepository
        .findById(cidadeId)
        .orElseThrow(() -> new CidadeNaoEncontradaException(cidadeId));
  }
}
