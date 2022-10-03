//package com.garcia.aula10testesintegracao;
//
//import com.garcia.aula10testesintegracao.domain.exception.EntidadeEmUsoException;
//import com.garcia.aula10testesintegracao.domain.exception.EntidadeNaoEncontradaException;
//import com.garcia.aula10testesintegracao.domain.model.Cozinha;
//import com.garcia.aula10testesintegracao.domain.service.CadastroCozinhaService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import javax.validation.ConstraintViolationException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//// TESTES DE INTEGRAÇÃO
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//class CadastroCozinhaIT {
//
//	@Autowired
//	private CadastroCozinhaService cadastroCozinhaService;
//
//	@Test
//	public void deveAtribuirId_QuandoCadastrarCozinhaComDadosCorretos() {
//		// cenário
//
//		Cozinha novaCozinha = new Cozinha();
//		novaCozinha.setNome("Chinesa");
//
//		// ação
//
//		novaCozinha = cadastroCozinhaService.salvar(novaCozinha);
//
//		// validação
//		assertThat(novaCozinha).isNotNull();
//		assertThat(novaCozinha.getId()).isNotNull();
//	}
//
//	@Test
//	public void deveFalhar_QuandoCadastrarCozinhaSemNome() {
//		Cozinha novaCozinha = new Cozinha();
//		novaCozinha.setNome(null);
//
//		ConstraintViolationException erroEsperado =
//				Assertions.assertThrows(ConstraintViolationException.class, () -> {
//					cadastroCozinhaService.salvar(novaCozinha);
//				});
//
//		assertThat(erroEsperado).isNotNull();
//	}
//
//	@Test
//	public void deveFalhar_QuandoExcluirCozinhaEmUso() {
//		// cenário
//		Cozinha novaCozinha = new Cozinha();
//		novaCozinha.setId(1l);
//
//		// ação
//
//
//		EntidadeEmUsoException erroEsperado =
//				Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
//					cadastroCozinhaService.excluir(novaCozinha.getId());
//				});
//
//		assertThat(erroEsperado).isNotNull();
//	}
//
//	@Test
//	public void deveFalhar_QuandoExcluirCozinhaInexistente(){
//		Cozinha novaCozinha = new Cozinha();
//		novaCozinha.setId(100l);
//
//		EntidadeNaoEncontradaException erroEsperado =
//				Assertions.assertThrows(EntidadeNaoEncontradaException.class, () -> {
//					cadastroCozinhaService.excluir(novaCozinha.getId());
//				});
//
//		assertThat(erroEsperado).isNotNull();
//	}
//}
