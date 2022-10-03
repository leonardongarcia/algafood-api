package com.garcia.algafoodapi.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Grupo {

  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;

  @Column(nullable = false)
  private String nome;

  @ManyToMany
  @JoinTable(
      name = "grupo_permissao",
      joinColumns = @JoinColumn(name = "grupo_id"),
      inverseJoinColumns = @JoinColumn(name = "permissao_id"))
  private Set<Permissao> permissoes = new HashSet<>();

  public boolean associarPermissao(Permissao permissao) {
    return getPermissoes().add(permissao);
  }

  public boolean dissociarPermissao(Permissao permissao) {
    return getPermissoes().remove(permissao);
  }
}
