package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.AlgaLinks;
import com.garcia.algafoodapi.api.v1.assembler.UsuarioModelAssembler;
import com.garcia.algafoodapi.api.v1.model.UsuarioModel;
import com.garcia.algafoodapi.api.v1.openapi.controller.RestauranteUsuarioControllerOpenApi;
import com.garcia.algafoodapi.core.security.AlgaSecurity;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.model.Restaurante;
import com.garcia.algafoodapi.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(
    path = "/v1/restaurantes/{restauranteId}/responsaveis",
    produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteUsuarioResponsavelController
    implements RestauranteUsuarioControllerOpenApi {

  @Autowired private CadastroRestauranteService cadastroRestauranteService;

  @Autowired private UsuarioModelAssembler usuarioModelAssembler;

  @Autowired private AlgaSecurity algaSecurity;

  @Autowired private AlgaLinks algaLinks;

  @CheckSecurity.Restaurantes.PodeGerenciarCadastro
  @Override
  @GetMapping
  public CollectionModel<UsuarioModel> listar(@PathVariable Long restauranteId) {
    Restaurante restaurante = cadastroRestauranteService.buscarOuFalhar(restauranteId);

    CollectionModel<UsuarioModel> usuariosModel =
        usuarioModelAssembler.toCollectionModel(restaurante.getResponsaveis()).removeLinks();

    usuariosModel.add(algaLinks.linkToRestauranteResponsaveis(restauranteId));

    if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
      usuariosModel.add(
          algaLinks.linkToRestauranteResponsavelAssociacao(restauranteId, "associar"));

      usuariosModel.getContent().stream()
          .forEach(
              usuarioModel -> {
                usuarioModel.add(
                    algaLinks.linkToRestauranteResponsavelDissociacao(
                        restauranteId, usuarioModel.getId(), "dissociar"));
              });
    }

    return usuariosModel;
  }

  @CheckSecurity.Restaurantes.PodeGerenciarCadastro
  @Override
  @DeleteMapping("/{responsavelId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> dissociar(
      @PathVariable Long restauranteId, @PathVariable Long responsavelId) {
    cadastroRestauranteService.dissociarUsuario(restauranteId, responsavelId);
    return ResponseEntity.noContent().build();
  }

  @CheckSecurity.Restaurantes.PodeGerenciarCadastro
  @Override
  @PutMapping("/{responsavelId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public ResponseEntity<Void> associar(
      @PathVariable Long restauranteId, @PathVariable Long responsavelId) {
    cadastroRestauranteService.associarUsuario(restauranteId, responsavelId);
    return ResponseEntity.noContent().build();
  }
}
