package com.garcia.algafoodapi.core.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties("algafood.email")
public class EmailProperties {

  // Atribuimos FAKE como padrão
  // Isso evita o problema de enviar e-mails de verdade caso você esqueça
  // de definir a propriedade
  private Implementacao impl = Implementacao.FAKE;

  @NotBlank private String remetente;

  private Sandbox sandbox = new Sandbox();

  public enum Implementacao {
    SMTP,
    FAKE,
    SANDBOX
  }

  @Getter
  @Setter
  public class Sandbox {

    private String destinatario;
  }
}
