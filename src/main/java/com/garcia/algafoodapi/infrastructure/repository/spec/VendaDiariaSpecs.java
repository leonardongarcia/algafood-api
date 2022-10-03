package com.garcia.algafoodapi.infrastructure.repository.spec;

import com.garcia.algafoodapi.domain.filter.VendaDiariaFilter;
import com.garcia.algafoodapi.domain.model.dto.VendaDiaria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;

public class VendaDiariaSpecs {

  public static Specification<VendaDiaria> usandoFiltro(VendaDiariaFilter vendaDiariaFilter) {
    return (root, query, criteriaBuilder) -> {
      var predicates = new ArrayList<Predicate>();

      if (vendaDiariaFilter.getRestauranteId() != null) {
        predicates.add(
            criteriaBuilder.equal(root.get("restaurante"), vendaDiariaFilter.getRestauranteId()));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
