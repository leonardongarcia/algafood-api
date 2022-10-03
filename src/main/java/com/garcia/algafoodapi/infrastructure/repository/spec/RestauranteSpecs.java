package com.garcia.algafoodapi.infrastructure.repository.spec;

import com.garcia.algafoodapi.domain.model.Restaurante;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class RestauranteSpecs {

  public static Specification<Restaurante> comFreteGratis() {
    return ((root, query, criteriaBuilder) ->
        criteriaBuilder.equal(root.get("taxaFrete"), BigDecimal.ZERO));
  }

  public static Specification<Restaurante> comNomeSemelhante(String nome) {
    return ((root, query, criteriaBuilder) ->
        criteriaBuilder.like(root.get("nome"), "%" + nome + "%"));
  }
}
