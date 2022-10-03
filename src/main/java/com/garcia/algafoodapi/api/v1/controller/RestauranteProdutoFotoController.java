package com.garcia.algafoodapi.api.v1.controller;

import com.garcia.algafoodapi.api.v1.assembler.FotoProdutoModelAssembler;
import com.garcia.algafoodapi.api.v1.model.FotoProdutoModel;
import com.garcia.algafoodapi.api.v1.model.input.FotoProdutoInput;
import com.garcia.algafoodapi.api.v1.openapi.controller.RestauranteProdutoFotoControllerOpenApi;
import com.garcia.algafoodapi.core.security.CheckSecurity;
import com.garcia.algafoodapi.domain.exception.EntidadeNaoEncontradaException;
import com.garcia.algafoodapi.domain.model.FotoProduto;
import com.garcia.algafoodapi.domain.model.Produto;
import com.garcia.algafoodapi.domain.service.CadastroProdutoService;
import com.garcia.algafoodapi.domain.service.CatalogoFotoProdutoService;
import com.garcia.algafoodapi.domain.service.FotoStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(
    path = "/v1/restaurantes/{restauranteId}/produtos/{produtoId}/foto",
    produces = MediaType.APPLICATION_JSON_VALUE)
public class RestauranteProdutoFotoController implements RestauranteProdutoFotoControllerOpenApi {

  @Autowired private CatalogoFotoProdutoService catalogoFotoProdutoService;

  @Autowired private CadastroProdutoService cadastroProdutoService;

  @Autowired private FotoProdutoModelAssembler fotoProdutoModelAssembler;

  @Autowired private FotoStorageService fotoStorageService;

  @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
  @Override
  @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public FotoProdutoModel atualizarFoto(
      @PathVariable Long restauranteId,
      @PathVariable Long produtoId,
      @Valid FotoProdutoInput fotoProdutoInput)
      throws IOException {

    Produto produto = cadastroProdutoService.buscarOuFalhar(produtoId, restauranteId);

    MultipartFile arquivo = fotoProdutoInput.getArquivo();

    FotoProduto fotoProduto = new FotoProduto();
    fotoProduto.setProduto(produto);
    fotoProduto.setDescricao(fotoProdutoInput.getDescricao());
    fotoProduto.setContentType(arquivo.getContentType());
    fotoProduto.setTamanho(arquivo.getSize());
    fotoProduto.setNomeArquivo(arquivo.getOriginalFilename());

    FotoProduto fotoSalva =
        catalogoFotoProdutoService.salvar(fotoProduto, arquivo.getInputStream());
    return fotoProdutoModelAssembler.toModel(fotoSalva);
  }

  @CheckSecurity.Restaurantes.PodeConsultar
  @GetMapping(
      produces = {
        MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE,
        MediaType.APPLICATION_JSON_VALUE
      })
  public ResponseEntity<?> buscar(
      @PathVariable Long restauranteId,
      @PathVariable Long produtoId,
      @RequestHeader(name = "accept") String acceptHeader)
      throws HttpMediaTypeNotAcceptableException {

    if (acceptHeader.equals(MediaType.APPLICATION_JSON_VALUE)) {
      return recuperarFoto(restauranteId, produtoId);
    }

    try {
      FotoProduto fotoProduto = catalogoFotoProdutoService.buscar(restauranteId, produtoId);
      MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());

      List<MediaType> mediaTypeAceitas = MediaType.parseMediaTypes(acceptHeader);
      verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypeAceitas);
      var fotoRecuperada = fotoStorageService.recuperar(fotoProduto.getNomeArquivo());
      if (fotoRecuperada.temUrl()) {

        return ResponseEntity.status(HttpStatus.FOUND)
            .header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
            .build();
      } else {
        return ResponseEntity.ok()
            .contentType(mediaTypeFoto)
            .body(new InputStreamResource(fotoRecuperada.getInputStream()));
      }
    } catch (EntidadeNaoEncontradaException e) {
      return ResponseEntity.notFound().build();
    }
  }

  public ResponseEntity<?> recuperarFoto(
      @PathVariable Long restauranteId, @PathVariable Long produtoId) {
    FotoProdutoModel fotoProdutoModel =
        fotoProdutoModelAssembler.toModel(
            catalogoFotoProdutoService.buscar(restauranteId, produtoId));
    return ResponseEntity.ok(fotoProdutoModel);
  }

  @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
  @Override
  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
    catalogoFotoProdutoService.deletar(produtoId, restauranteId);
  }

  private void verificarCompatibilidadeMediaType(
      MediaType mediaTypeFoto, List<MediaType> mediaTypesAceitas)
      throws HttpMediaTypeNotAcceptableException {

    boolean compativel =
        mediaTypesAceitas.stream()
            .anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));

    if (!compativel) {
      throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
    }
  }
}
