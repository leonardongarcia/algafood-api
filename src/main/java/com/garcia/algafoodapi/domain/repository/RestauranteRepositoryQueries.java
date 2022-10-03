package com.garcia.algafoodapi.domain.repository;

import com.garcia.algafoodapi.domain.model.Restaurante;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RestauranteRepositoryQueries {
  List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

  List<Restaurante> findComFreteGratis(String nome);
}
