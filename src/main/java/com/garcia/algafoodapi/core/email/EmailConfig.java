package com.garcia.algafoodapi.core.email;

import com.garcia.algafoodapi.domain.service.EnvioEmailService;
import com.garcia.algafoodapi.infrastructure.service.email.FakeEnvioEmailService;
import com.garcia.algafoodapi.infrastructure.service.email.SandBoxEnvioEmailService;
import com.garcia.algafoodapi.infrastructure.service.email.SmtpEnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

  @Autowired private EmailProperties emailProperties;

  @Bean
  public EnvioEmailService envioEmailService() {
    // Acho melhor usar switch aqui do que if/else if
    switch (emailProperties.getImpl()) {
      case FAKE:
        return new FakeEnvioEmailService();
      case SMTP:
        return new SmtpEnvioEmailService();
      case SANDBOX:
        return new SandBoxEnvioEmailService();
      default:
        return null;
    }
  }
}
