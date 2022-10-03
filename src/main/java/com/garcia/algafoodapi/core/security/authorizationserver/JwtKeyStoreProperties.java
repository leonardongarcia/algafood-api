package com.garcia.algafoodapi.core.security.authorizationserver;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Component
@ConfigurationProperties("algafood.jwt.keystore")
public class JwtKeyStoreProperties {

  @NotBlank private Resource jksLocation;

  @NotBlank private String password;

  @NotBlank private String keyPairAlias;
}
