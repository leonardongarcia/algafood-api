package com.garcia.algafoodapi.domain.model;

import com.garcia.algafoodapi.core.validation.ValorZeroIncluiDescricao;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ValorZeroIncluiDescricao(
    valorField = "taxaFrete",
    descricaoField = "nome",
    descricaoObrigatoria = "Frete Grátis")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "restaurante")
public class Restaurante {

  @Id
  @EqualsAndHashCode.Include
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String nome;

  @PositiveOrZero
  @Column(name = "taxa_frete", nullable = false)
  private BigDecimal taxaFrete;

  @ManyToOne // (fetch = FetchType.LAZY)
  @JoinColumn(name = "cozinha_id", nullable = false)
  private Cozinha cozinha;

  @CreationTimestamp
  @Column(nullable = false, columnDefinition = "datetime")
  private OffsetDateTime dataCadastro;

  @UpdateTimestamp
  @Column(nullable = false, columnDefinition = "datetime")
  private OffsetDateTime dataAtualizacao;

  @Embedded private Endereco endereco;

  private Boolean ativo = Boolean.TRUE;

  private Boolean aberto = Boolean.FALSE;

  @OneToMany(mappedBy = "restaurante")
  private List<Produto> produtos = new ArrayList<>();

  @ManyToMany
  @JoinTable(
      name = "restaurante_forma_pagamento",
      joinColumns = @JoinColumn(name = "restaurante_id"),
      inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
  private Set<FormaPagamento> formasPagamento = new HashSet<>();

  @ManyToMany
  @JoinTable(
      name = "restaurante_usuario_responsavel",
      joinColumns = @JoinColumn(name = "restaurante_id"),
      inverseJoinColumns = @JoinColumn(name = "usuario_id"))
  private Set<Usuario> responsaveis = new HashSet<>();

  public void ativar() {
    setAtivo(true);
  }

  public void inativar() {
    setAtivo(false);
  }

  public void abrir() {
    setAberto(true);
  }

  public void fechar() {
    setAberto(false);
  }

  public void dissociarFormaPagamento(FormaPagamento formaPagamento) {
    getFormasPagamento().remove(formaPagamento);
  }

  public void associarFormaPagamento(FormaPagamento formaPagamento) {
    getFormasPagamento().add(formaPagamento);
  }

  public void associarUsuario(Usuario usuario) {
    this.getResponsaveis().add(usuario);
  }

  public void dissociarUsuario(Usuario usuario) {
    this.getResponsaveis().remove(usuario);
  }

  public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
    return getFormasPagamento().contains(formaPagamento);
  }

  public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
    return !aceitaFormaPagamento(formaPagamento);
  }

  public boolean isAberto() {
    return this.aberto;
  }

  public boolean isFechado() {
    return !isAberto();
  }

  public boolean isInativo() {
    return !isAtivo();
  }

  public boolean isAtivo() {
    return this.ativo;
  }

  public boolean aberturaPermitida() {
    return isAtivo() && isFechado();
  }

  public boolean ativacaoPermitida() {
    return isInativo();
  }

  public boolean inativacaoPermitida() {
    return isAtivo();
  }

  public boolean fechamentoPermitido() {
    return isAberto();
  }
}
