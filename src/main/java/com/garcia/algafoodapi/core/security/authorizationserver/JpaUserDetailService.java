package com.garcia.algafoodapi.core.security.authorizationserver;

import com.garcia.algafoodapi.domain.model.Usuario;
import com.garcia.algafoodapi.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailService implements UserDetailsService {

  @Autowired private UsuarioRepository usuarioRepository;

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario =
        usuarioRepository
            .findByEmail(username)
            .orElseThrow(
                () -> new UsernameNotFoundException("Usuário não encontrado com e-mail informado"));

    return new User(usuario.getEmail(), usuario.getSenha(), getAuthorities(usuario));
  }

  private Collection<GrantedAuthority> getAuthorities(Usuario usuario) {
    return usuario.getGrupos().stream()
        .flatMap(grupo -> grupo.getPermissoes().stream())
        .map(permissao -> new SimpleGrantedAuthority(permissao.getNome().toUpperCase(Locale.ROOT)))
        .collect(Collectors.toSet());
  }
}
