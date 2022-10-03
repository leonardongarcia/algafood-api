package com.garcia.algafoodapi;

import com.garcia.algafoodapi.domain.model.Cozinha;
import com.garcia.algafoodapi.domain.model.Restaurante;
import com.garcia.algafoodapi.domain.repository.CozinhaRepository;
import com.garcia.algafoodapi.domain.repository.RestauranteRepository;
import com.garcia.algafoodapi.util.DatabaseCleaner;
import com.garcia.algafoodapi.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
public class CadastroRestauranteIT {

  private static final String VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE =
      "Violação de regra de negócio!";

  private static final String DADOS_INVALIDOS_PROBLEM_TITLE = "Dados inválidos!";

  private static final int RESTAURANTE_ID_INEXISTENTE = 100;

  @LocalServerPort private int port;

  @Autowired private DatabaseCleaner databaseCleaner;

  @Autowired private RestauranteRepository restauranteRepository;

  @Autowired private CozinhaRepository cozinhaRepository;

  private String jsonCorretoRestauranteMineiro;
  private String jsonRestauranteCorreto;
  private String jsonRestauranteSemFrete;
  private String jsonRestauranteSemCozinha;
  private String jsonRestauranteComCozinhaInexistente;
  private Restaurante burgerTopRestaurante = new Restaurante();

  @BeforeEach
  public void setUp() {
    enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.port = port;
    basePath = "/restaurantes";

    databaseCleaner.clearTables();
    prepararDados();

    jsonRestauranteCorreto =
        ResourceUtils.getContentFromResource("/json/correto/restaurante-new-york-barbecue.json");

    jsonRestauranteSemFrete =
        ResourceUtils.getContentFromResource(
            "/json/incorreto/restaurante-new-york-barbecue-sem-frete.json");

    jsonRestauranteSemCozinha =
        ResourceUtils.getContentFromResource(
            "/json/incorreto/restaurante-new-york-barbecue-sem-cozinha.json");

    jsonRestauranteComCozinhaInexistente =
        ResourceUtils.getContentFromResource(
            "/json/incorreto/restaurante-new-york-barbecue-com-cozinha-inexistente.json");
    jsonCorretoRestauranteMineiro =
        ResourceUtils.getContentFromResource("/json/correto/restaurante-mineiro.json");
  }

  @Test
  public void deveRetornarStatus200_QuandoConsultarRestaurante() {
    given().accept(ContentType.JSON).when().get().then().statusCode(HttpStatus.OK.value());
  }

  @Test
  public void deveRetornarStatus201_QuandoCadastrarRestaurante() {
    given()
        .body(jsonCorretoRestauranteMineiro)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .post()
        .then()
        .statusCode(HttpStatus.CREATED.value());
  }

  @Test
  public void deveRetornarStatus400_QuandoCadastrarRestauranteIncorreto() {
    given()
        .body("sadfasdf")
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .post()
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }

  @Test
  public void deveRetornarStatus400_QuandoCadastrarRestauranteComCozinhaInexistente() {
    given()
        .body(jsonRestauranteComCozinhaInexistente)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .post()
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body("title", equalTo(VIOLACAO_DE_REGRA_DE_NEGOCIO_PROBLEM_TYPE));
  }

  @Test
  public void deveRetornarRespostaEStatusCorretos_QuandoConsultarRestauranteExistente() {
    given()
        .pathParam("restauranteId", burgerTopRestaurante.getId())
        .accept(ContentType.JSON)
        .when()
        .get("/{restauranteId}")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("nome", equalTo(burgerTopRestaurante.getNome()));
  }

  @Test
  public void deveRetornarStatus404_QuandoConsultarRestauranteInexistente() {
    given()
        .pathParam("restauranteId", RESTAURANTE_ID_INEXISTENTE)
        .accept(ContentType.JSON)
        .when()
        .get("/{restauranteId}")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  @Test
  public void deveRetornarStatus400_QuandoCadastrarRestauranteSemTaxaFrete() {
    given()
        .body(jsonRestauranteSemFrete)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .post()
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
  }

  @Test
  public void deveRetornarStatus400_QuandoCadastrarRestauranteSemCozinha() {
    given()
        .body(jsonRestauranteSemCozinha)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .post()
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .body("title", equalTo(DADOS_INVALIDOS_PROBLEM_TITLE));
  }

  @Test
  public void deveRetornarStatus405_QuandoTentarExcluirRestaurante() {
    given()
        .pathParam("restauranteId", 1)
        .accept(ContentType.JSON)
        .when()
        .delete("/{restauranteId}")
        .then()
        .statusCode(HttpStatus.METHOD_NOT_ALLOWED.value());
  }

  private void prepararDados() {
    Cozinha cozinha = new Cozinha();
    cozinha.setNome("Tailandesa");
    cozinhaRepository.save(cozinha);

    Restaurante restaurante = new Restaurante();
    restaurante.setNome("Restaurante do Hélio");
    restaurante.setCozinha(cozinha);
    restaurante.setTaxaFrete(new BigDecimal(13));
    restauranteRepository.save(restaurante);

    Cozinha cozinhaBrasileira = new Cozinha();
    cozinhaBrasileira.setNome("Brasileira");
    cozinhaRepository.save(cozinhaBrasileira);

    Cozinha cozinhaAmericana = new Cozinha();
    cozinhaAmericana.setNome("Americana");
    cozinhaRepository.save(cozinhaAmericana);

    //        burgerTopRestaurante = new Restaurante();
    burgerTopRestaurante.setNome("Burger Top");
    burgerTopRestaurante.setTaxaFrete(new BigDecimal(10));
    burgerTopRestaurante.setCozinha(cozinhaAmericana);
    restauranteRepository.save(burgerTopRestaurante);

    Restaurante comidaMineiraRestaurante = new Restaurante();
    comidaMineiraRestaurante.setNome("Comida Mineira");
    comidaMineiraRestaurante.setTaxaFrete(new BigDecimal(10));
    comidaMineiraRestaurante.setCozinha(cozinhaBrasileira);
    restauranteRepository.save(comidaMineiraRestaurante);
  }
}
