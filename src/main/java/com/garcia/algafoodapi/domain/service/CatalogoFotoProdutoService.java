package com.garcia.algafoodapi.domain.service;

import com.garcia.algafoodapi.domain.exception.FotoProdutoNaoEncontradoException;
import com.garcia.algafoodapi.domain.model.FotoProduto;
import com.garcia.algafoodapi.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Optional;

import static com.garcia.algafoodapi.domain.service.FotoStorageService.NovaFoto;

@Service
public class CatalogoFotoProdutoService {

  @Autowired private ProdutoRepository produtoRepository;

  @Autowired private FotoStorageService fotoStorageService;

  @Transactional
  public FotoProduto salvar(FotoProduto fotoProduto, InputStream dadosArquivo) {
    Long restauranteId = fotoProduto.getRestauranteId();
    Long produtoId = fotoProduto.getProduto().getId();
    String nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(fotoProduto.getNomeArquivo());
    String nomeArquivoExistente = null;

    Optional<FotoProduto> fotoExistente = produtoRepository.findFotoById(restauranteId, produtoId);

    if (fotoExistente.isPresent()) {
      nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
      produtoRepository.delete(fotoExistente.get());
    }

    fotoProduto.setNomeArquivo(nomeNovoArquivo);
    produtoRepository.save(fotoProduto);
    produtoRepository.flush();

    NovaFoto novaFoto =
        NovaFoto.builder()
            .nomeArquivo(fotoProduto.getNomeArquivo())
            .contentType(fotoProduto.getContentType())
            .inputStream(dadosArquivo)
            .build();

    fotoStorageService.substituir(nomeArquivoExistente, novaFoto);

    return fotoProduto;
  }

  public FotoProduto buscar(Long produtoId, Long restauranteId) {
    return produtoRepository
        .findFotoById(restauranteId, produtoId)
        .orElseThrow(() -> new FotoProdutoNaoEncontradoException(produtoId, restauranteId));
  }

  public void deletar(Long produtoId, Long restauranteId) {
    FotoProduto fotoProduto = buscar(produtoId, restauranteId);
    produtoRepository.delete(fotoProduto);
    produtoRepository.flush();
    fotoStorageService.remover(fotoProduto.getNomeArquivo());
  }
}
