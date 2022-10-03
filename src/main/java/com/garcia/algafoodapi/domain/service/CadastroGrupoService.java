package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.garcia.algafoodapi.domain.exception.GrupoNaoEncontradoException;
import com.garcia.algafoodapi.domain.exception.PermissaoNaoEncontradaException;
import com.garcia.algafoodapi.domain.model.Grupo;
import com.garcia.algafoodapi.domain.model.Permissao;
import com.garcia.algafoodapi.domain.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CadastroGrupoService {

  public static final String MSG_GRUPO_EM_USO =
      "O grupo de código %d não pode ser removido," + " pois está em uso";

  @Autowired private GrupoRepository grupoRepository;

  @Autowired private CadastroPermissaoService cadastroPermissaoService;

  public List<Grupo> listar() {
    return grupoRepository.findAll();
  }

  public Grupo buscarOuFalhar(Long grupoId) {
    return grupoRepository
        .findById(grupoId)
        .orElseThrow(() -> new GrupoNaoEncontradoException(grupoId));
  }

  @Transactional
  public Grupo save(Grupo grupo) {
    return grupoRepository.save(grupo);
  }

  @Transactional
  public void excluir(Long grupoId) {
    try {
      grupoRepository.deleteById(grupoId);
      grupoRepository.flush();
    } catch (EmptyResultDataAccessException e) {
      throw new GrupoNaoEncontradoException(grupoId);
    } catch (DataIntegrityViolationException e) {
      throw new EntidadeEmUsoException(String.format(MSG_GRUPO_EM_USO, grupoId));
    }
  }

  @Transactional
  public void dissociarPermissao(Long grupoId, Long permissaoId) {
    Grupo grupo = buscarOuFalhar(grupoId);
    Permissao permissao = cadastroPermissaoService.buscarOuFalhar(permissaoId);
    Set<Permissao> permissoes = grupo.getPermissoes();
    if (!permissoes.contains(permissao)) {
      throw new PermissaoNaoEncontradaException(
          String.format(
              "Não existe um cadastro de permissão com o código %d " + "para o grupo de código %d",
              permissaoId, grupoId));
    }
    grupo.dissociarPermissao(permissao);
  }

  @Transactional
  public void associarPermissao(Long grupoId, Long permissaoId) {
    Grupo grupo = buscarOuFalhar(grupoId);
    Permissao permissao = cadastroPermissaoService.buscarOuFalhar(permissaoId);
    grupo.associarPermissao(permissao);
  }
}
