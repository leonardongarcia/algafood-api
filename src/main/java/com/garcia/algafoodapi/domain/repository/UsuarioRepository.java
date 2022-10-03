package com.garcia.algafoodapi.domain.repository;

import com.garcia.algafoodapi.domain.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long> {

  Optional<Usuario> findByEmail(String email);
}
