package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.exception.EntidadeEmUsoException;
import com.garcia.algafoodapi.domain.exception.GrupoNaoEncontradoException;
import com.garcia.algafoodapi.domain.exception.NegocioException;
import com.garcia.algafoodapi.domain.exception.UsuarioNaoEncontradoException;
import com.garcia.algafoodapi.domain.model.Grupo;
import com.garcia.algafoodapi.domain.model.Usuario;
import com.garcia.algafoodapi.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CadastroUsuarioService {

  public static final String MSG_USUARIO_EM_USO =
      "O usuário com código %d não pode ser removido," + " pois está em uso";

  @Autowired private UsuarioRepository usuarioRepository;

  @Autowired private CadastroGrupoService cadastroGrupoService;

  @Autowired private PasswordEncoder passwordEncoder;

  public List<Usuario> listar() {
    return usuarioRepository.findAll();
  }

  public Usuario buscarOuFalhar(Long usuarioId) {
    return usuarioRepository
        .findById(usuarioId)
        .orElseThrow(() -> new UsuarioNaoEncontradoException(usuarioId));
  }

  @Transactional
  public Usuario save(Usuario usuario) {
    usuarioRepository.detach(usuario);

    Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());

    if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
      throw new NegocioException(
          String.format("Já existe um usuário cadastrado com o email %s", usuario.getEmail()));
    }

    if (usuario.isNovo()) {
      usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
    }
    return usuarioRepository.save(usuario);
  }

  @Transactional
  public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
    Usuario usuario = buscarOuFalhar(usuarioId);

    if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
      throw new NegocioException("Senha atual informada não coincide com a senha do usuário");
    }
    usuario.setSenha(passwordEncoder.encode(novaSenha));
  }

  @Transactional
  public void excluir(Long usuarioId) {
    try {
      usuarioRepository.deleteById(usuarioId);
      usuarioRepository.flush();
    } catch (DataIntegrityViolationException e) {
      throw new EntidadeEmUsoException(String.format(MSG_USUARIO_EM_USO, usuarioId));
    } catch (EmptyResultDataAccessException e) {
      throw new UsuarioNaoEncontradoException(usuarioId);
    }
  }

  @Transactional
  public void associarGrupo(Long usuarioId, Long grupoId) {
    Usuario usuario = buscarOuFalhar(usuarioId);
    Grupo grupo = cadastroGrupoService.buscarOuFalhar(grupoId);
    usuario.associarGrupo(grupo);
  }

  @Transactional
  public void dissociarGrupo(Long usuarioId, Long grupoId) {
    Usuario usuario = buscarOuFalhar(usuarioId);
    Grupo grupo = cadastroGrupoService.buscarOuFalhar(grupoId);
    Set<Grupo> grupos = usuario.getGrupos();
    if (!grupos.contains(grupo)) {
      throw new GrupoNaoEncontradoException(
          String.format(
              "Não existe um cadastro de grupo com o código %d " + "para o usuário de código %d",
              grupoId, usuarioId));
    }
    usuario.dissociarGrupo(grupo);
  }
}
