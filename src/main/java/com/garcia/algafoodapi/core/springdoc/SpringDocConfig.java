package com.garcia.algafoodapi.core.springdoc;

import com.garcia.algafoodapi.api.exceptionhandler.Problem;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@SecurityScheme(
    name = "security_auth",
    type = SecuritySchemeType.OAUTH2,
    flows =
        @OAuthFlows(
            authorizationCode =
                @OAuthFlow(
                    authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
                    tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",
                    scopes = {
                      @OAuthScope(name = "READ", description = "read scope"),
                      @OAuthScope(name = "WRITE", description = "write scope"),
                    })))
public class SpringDocConfig {

    public static final String badRequestResponse = "BadRequestResponse";
    public static final String notFoundResponse = "NotFoundResponse";
    public static final String notAcceptableResponse = "NotAcceptableResponse";
    public static final String internalServerErrorResponse = "InternalServerErrorResponse";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("AlgaFood API")
                .version("v1")
                .description("REST Api do AlgaFood")
                .license(new License().name("Apcache 2.0").url("http://springdoc.com")))
        .externalDocs(
            new ExternalDocumentation().description("AlgaWorks").url("http://algaworks.com"))
        .tags(Arrays.asList(
                new Tag().name("Cidades").description("Gerencia as cidades"),
                new Tag().name("Grupos").description("Gerencia os grupos"),
                new Tag().name("Cozinhas").description("Gerencia as cozinhas"),
                new Tag().name("Formas de Pagamento").description("Gerencia as formas de pagamento"),
                new Tag().name("Pedidos").description("Gerencia os pedidos"),
                new Tag().name("Restaurantes").description("Gerencia os restaurantes"),
                new Tag().name("Estados").description("Gerencia os estados"),
                new Tag().name("Produtos").description("Gerencia os produtos"),
                new Tag().name("Usu??rios").description("Gerencia os usu??rios"),
                new Tag().name("Estat??sticas").description("Estat??sticas da AlgaFood"),
                new Tag().name("Permiss??es").description("Gerencia as permiss??es")))
            .components(new Components().schemas(
                    gerarSchemas()
            ).responses(
                    gerarResponses()
            ));
  }


    @Bean
  public OpenApiCustomiser openApiCustomiser() {
    return openApi ->
        openApi
            .getPaths()
            .values()
            .forEach(
                pathItem ->
                    pathItem
                        .readOperationsMap()
                        .forEach(
                            (httpMethod, operation) -> {
                              ApiResponses responses = operation.getResponses();
                              switch (httpMethod) {
                                  case GET -> {
                                      responses.addApiResponse("406", new ApiResponse().$ref(notAcceptableResponse));
                                      responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                  }
                                  case POST, PUT -> {
                                      responses.addApiResponse("400", new ApiResponse().$ref(badRequestResponse));
                                      responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                                  }
                                  default -> responses.addApiResponse("500", new ApiResponse().$ref(internalServerErrorResponse));
                              }
                            }));
  }

  private Map<String, Schema> gerarSchemas(){
      final Map<String, Schema> schemaMap = new HashMap<>();

      Map<String, Schema> problemSchema = ModelConverters.getInstance().read(Problem.class);
      Map<String, Schema> problemObjectSchema = ModelConverters.getInstance().read(Problem.Object.class);

      schemaMap.putAll(problemSchema);
      schemaMap.putAll(problemObjectSchema);

      return schemaMap;
  }

  private Map<String, ApiResponse> gerarResponses() {
      final Map<String, ApiResponse> apiResponseMap = new HashMap<>();

      Content content = new Content()
              .addMediaType(APPLICATION_JSON_VALUE, new MediaType().schema(new Schema<Problem>().$ref("Problema")));

      apiResponseMap.put(badRequestResponse, new ApiResponse()
              .description("Requisi????o inv??lida")
              .content(content));

      apiResponseMap.put(notFoundResponse, new ApiResponse()
              .description("Recurso n??o encontrado")
              .content(content));

      apiResponseMap.put(notAcceptableResponse, new ApiResponse()
              .description("Recurso n??o possui representa????o que poderia ser aceita pelo consumidor")
              .content(content));

      apiResponseMap.put(internalServerErrorResponse, new ApiResponse()
              .description("Erro interno no servidor")
              .content(content));

      return apiResponseMap;
  }
}
