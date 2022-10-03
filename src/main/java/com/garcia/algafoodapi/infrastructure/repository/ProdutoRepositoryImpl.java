package com.garcia.algafoodapi.infrastructure.repository;

import com.garcia.algafoodapi.domain.model.FotoProduto;
import com.garcia.algafoodapi.domain.repository.ProdutoRepositoryQueries;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryQueries {

  @PersistenceContext private EntityManager entityManager;

  @Transactional
  @Override
  public FotoProduto save(FotoProduto fotoProduto) {
    return entityManager.merge(fotoProduto);
  }

  @Transactional
  @Override
  public void delete(FotoProduto fotoProduto) {
    entityManager.remove(fotoProduto);
  }
}
