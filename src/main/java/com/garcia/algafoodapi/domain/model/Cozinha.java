package com.garcia.algafoodapi.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "cozinha")
public class Cozinha {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nome", nullable = false)
  private String nome;

  @OneToMany(mappedBy = "cozinha")
  private List<Restaurante> restaurantes = new ArrayList<>();
}
