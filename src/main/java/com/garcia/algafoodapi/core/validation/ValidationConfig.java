package com.garcia.algafoodapi.core.validation;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

// Classe para juntar os dois arquivos de configuração "messages.properties" e
// "ValidatorMessages.properties"
@Configuration
public class ValidationConfig {

  @Bean
  public LocalValidatorFactoryBean validator(MessageSource messageSource) {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource);
    return bean;
  }
}
