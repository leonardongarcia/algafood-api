package com.garcia.algafoodapi.domain.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

public interface EnvioEmailService {

  void enviar(Mensagem mensagem);

  @Getter
  @Builder
  class Mensagem {

    @Singular private Set<String> destinatarios;

    @NotNull private String corpo;

    @NotNull private String assunto;

    @Singular("variavel")
    private Map<String, Object> variaveis;
  }
}
