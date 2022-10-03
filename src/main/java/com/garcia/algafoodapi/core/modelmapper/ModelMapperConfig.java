package com.garcia.algafoodapi.core.modelmapper;

import com.garcia.algafoodapi.api.v1.model.EnderecoModel;
import com.garcia.algafoodapi.api.v1.model.input.ItemPedidoInput;
import com.garcia.algafoodapi.api.v2.model.input.CidadeInputV2;
import com.garcia.algafoodapi.domain.model.Cidade;
import com.garcia.algafoodapi.domain.model.Endereco;
import com.garcia.algafoodapi.domain.model.ItemPedido;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper modelMapper() {
    var modelMapper = new ModelMapper();

    var enderecoToEnderecoModelTypeMap =
        modelMapper.createTypeMap(Endereco.class, EnderecoModel.class);

    modelMapper
        .createTypeMap(CidadeInputV2.class, Cidade.class)
        .addMappings(mapper -> mapper.skip(Cidade::setId));

    modelMapper
        .createTypeMap(ItemPedidoInput.class, ItemPedido.class)
        .addMappings(mapper -> mapper.skip(ItemPedido::setId));

    enderecoToEnderecoModelTypeMap.<String>addMapping(
        enderecoSrc -> enderecoSrc.getCidade().getEstado().getNome(),
        (enderecoModelDest, value) -> enderecoModelDest.getCidade().setEstado(value));

    return modelMapper;
  }
}
