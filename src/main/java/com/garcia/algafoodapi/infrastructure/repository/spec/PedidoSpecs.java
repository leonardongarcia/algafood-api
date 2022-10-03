package com.garcia.algafoodapi.infrastructure.repository.spec;

import com.garcia.algafoodapi.domain.filter.PedidoFilter;
import com.garcia.algafoodapi.domain.model.Pedido;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;

public class PedidoSpecs {

  public static Specification<Pedido> usandoFiltro(PedidoFilter pedidoFilter) {
    return (root, query, criteriaBuilder) -> {
      if (Pedido.class.equals(query.getResultType())) {
        root.fetch("restaurante").fetch("cozinha");
        root.fetch("cliente");
      }

      var predicates = new ArrayList<Predicate>();

      if (pedidoFilter.getClienteId() != null) {
        predicates.add(criteriaBuilder.equal(root.get("cliente"), pedidoFilter.getClienteId()));
      }

      if (pedidoFilter.getRestauranteId() != null) {
        predicates.add(
            criteriaBuilder.equal(root.get("restaurante"), pedidoFilter.getRestauranteId()));
      }

      if (pedidoFilter.getDataCriacaoInicio() != null) {
        predicates.add(
            criteriaBuilder.greaterThanOrEqualTo(
                root.get("dataCriacao"), pedidoFilter.getDataCriacaoInicio()));
      }

      if (pedidoFilter.getDataCriacaoFim() != null) {
        predicates.add(
            criteriaBuilder.lessThanOrEqualTo(
                root.get("dataCriacao"), pedidoFilter.getDataCriacaoFim()));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
