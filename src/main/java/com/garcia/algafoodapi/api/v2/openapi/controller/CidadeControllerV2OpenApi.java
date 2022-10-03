package com.garcia.algafoodapi.api.v2.openapi.controller;

import com.garcia.algafoodapi.api.v2.model.CidadeModelV2;
import com.garcia.algafoodapi.api.v2.model.input.CidadeInputV2;
import org.springframework.hateoas.CollectionModel;

public interface CidadeControllerV2OpenApi {

  CollectionModel<CidadeModelV2> listar();

  CidadeModelV2 buscar(Long cidadeId);

  CidadeModelV2 adicionar(CidadeInputV2 cidadeInputV2);

  CidadeModelV2 atualizar(Long cidadeId, CidadeInputV2 cidadeInputV2);

  void excluir(Long cidadeId);
}
