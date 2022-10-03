package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.exception.PermissaoNaoEncontradaException;
import com.garcia.algafoodapi.domain.model.Permissao;
import com.garcia.algafoodapi.domain.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CadastroPermissaoService {

  @Autowired private PermissaoRepository permissaoRepository;

  public List<Permissao> listar() {
    return permissaoRepository.findAll();
  }

  public Permissao buscarOuFalhar(Long permissaoId) {
    return permissaoRepository
        .findById(permissaoId)
        .orElseThrow(() -> new PermissaoNaoEncontradaException(permissaoId));
  }
}
