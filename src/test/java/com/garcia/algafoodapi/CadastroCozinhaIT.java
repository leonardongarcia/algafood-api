package com.garcia.algafoodapi;

import com.garcia.algafoodapi.domain.model.Cozinha;
import com.garcia.algafoodapi.domain.repository.CozinhaRepository;
import com.garcia.algafoodapi.util.DatabaseCleaner;
import com.garcia.algafoodapi.util.ResourceUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

// TESTES DE API

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CadastroCozinhaIT {

  @LocalServerPort private int port;

  @Autowired private DatabaseCleaner databaseCleaner;

  @Autowired private CozinhaRepository cozinhaRepository;

  private static final int COZINHA_ID_INEXISTENTE = 100;

  private Cozinha cozinhaAmericana;
  private int quantidadeCozinhasCadastradas;
  private String jsonCorretoCozinhaChinesa;

  @BeforeEach
  public void setUp() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    RestAssured.port = port;
    RestAssured.basePath = "/cozinhas";

    databaseCleaner.clearTables();
    prepararDados();

    jsonCorretoCozinhaChinesa =
        ResourceUtils.getContentFromResource("/json/correto/cozinha-chinesa.json");
  }

  @Test
  public void deveRetornarRespostaEStatusCorretos_QuandoConsultarCozinhaExistente() {

    given()
        .pathParam("cozinhaId", cozinhaAmericana.getId())
        .accept(ContentType.JSON)
        .when()
        .get("/{cozinhaId}")
        .then()
        .statusCode(HttpStatus.OK.value())
        .body("nome", equalTo(cozinhaAmericana.getNome()));
  }

  @Test
  public void deveRetornarStaus404_QuandoConsultarCozinhaInexistente() {
    given()
        .pathParam("cozinhaId", COZINHA_ID_INEXISTENTE)
        .accept(ContentType.JSON)
        .when()
        .get("/{cozinhaId}")
        .then()
        .statusCode(HttpStatus.NOT_FOUND.value());
  }

  @Test
  public void deveRetornarStatus200_QuandoConsultarCozinhas() {

    given().accept(ContentType.JSON).when().get().then().statusCode(HttpStatus.OK.value());
  }

  @Test
  public void deveConterTotalCozinhas_QuandoConsultarCozinhas() {

    given()
        .accept(ContentType.JSON)
        .when()
        .get()
        .then()
        .body("", Matchers.hasSize(quantidadeCozinhasCadastradas))
        .body("nome", Matchers.hasItems("Tailandesa", "Americana"));
  }

  @Test
  public void deveRetornarStatus201_QuandoCadastrarCozinhas() {

    given()
        .body(jsonCorretoCozinhaChinesa)
        .contentType(ContentType.JSON)
        .accept(ContentType.JSON)
        .when()
        .post()
        .then()
        .statusCode(HttpStatus.CREATED.value());
  }

  private void prepararDados() {

    Cozinha cozinha = new Cozinha();
    cozinha.setNome("Tailandesa");
    cozinhaRepository.save(cozinha);

    cozinhaAmericana = new Cozinha();
    cozinhaAmericana.setNome("Americana");
    cozinhaRepository.save(cozinhaAmericana);

    quantidadeCozinhasCadastradas = (int) cozinhaRepository.count();
  }
}
