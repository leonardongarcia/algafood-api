package com.garcia.algafoodapi.domain.repository;

import com.garcia.algafoodapi.domain.model.FotoProduto;

public interface ProdutoRepositoryQueries {

  FotoProduto save(FotoProduto fotoProduto);

  void delete(FotoProduto fotoProduto);
}
