package com.garcia.algafoodapi.infrastructure.repository;

import com.garcia.algafoodapi.domain.model.Restaurante;
import com.garcia.algafoodapi.domain.repository.RestauranteRepository;
import com.garcia.algafoodapi.domain.repository.RestauranteRepositoryQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.garcia.algafoodapi.infrastructure.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.garcia.algafoodapi.infrastructure.repository.spec.RestauranteSpecs.comNomeSemelhante;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

  @PersistenceContext private EntityManager entityManager;

  @Autowired @Lazy private RestauranteRepository restauranteRepository;

  @Override
  public List<Restaurante> find(
      String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

    CriteriaBuilder builder = entityManager.getCriteriaBuilder();

    CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
    Root<Restaurante> root = criteria.from(Restaurante.class); // from Restaurante

    var predicates = new ArrayList<Predicate>();

    if (StringUtils.hasText(nome)) {
      predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
    }

    if (taxaFreteInicial != null) {
      predicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));
    }

    if (taxaFreteFinal != null) {
      predicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
    }

    criteria.where(predicates.toArray(new Predicate[0]));

    TypedQuery<Restaurante> query = entityManager.createQuery(criteria);
    return query.getResultList();
  }

  @Override
  public List<Restaurante> findComFreteGratis(String nome) {
    return restauranteRepository.findAll(comFreteGratis().and(comNomeSemelhante(nome)));
  }
}
