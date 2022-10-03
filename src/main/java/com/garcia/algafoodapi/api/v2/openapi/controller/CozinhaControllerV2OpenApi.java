package com.garcia.algafoodapi.api.v2.openapi.controller;

import com.garcia.algafoodapi.api.v2.model.CozinhaModelV2;
import com.garcia.algafoodapi.api.v2.model.input.CozinhaInputV2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;

public interface CozinhaControllerV2OpenApi {
  PagedModel<CozinhaModelV2> listar(@PageableDefault(size = 10) Pageable pageable);

  CozinhaModelV2 buscar(Long cozinhaId);

  CozinhaModelV2 adicionar(CozinhaInputV2 cozinhaInputV2);

  CozinhaModelV2 atualizar(Long cozinhaId, CozinhaInputV2 cozinhaInputV2);

  void excluir(Long cozinhaId);
}
