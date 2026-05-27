package br.com.ifpe.banco.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import br.com.ifpe.banco.entity.enums.EnumEstadoCivil;
import br.com.ifpe.banco.entity.enums.EnumGenero;

@Entity
@Table(name = "clientes", uniqueConstraints = {
    @UniqueConstraint(columnNames = "cpf"),
    @UniqueConstraint(columnNames = "email")
})
public class Cliente implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @NotBlank
  @Size(max = 150)
  @Column(name = "nome_completo", nullable = false, length = 150)
  private String nomeCompleto;
  
  @NotBlank
  @Size(min = 11, max = 14)
  @Column(name = "cpf", nullable = false, length = 14)
  private String cpf;
  
  @NotBlank
  @Email
  @Size(max = 120)
  @Column(name = "email", nullable = false, length = 120)
  private String email;
  
  @Size(max = 20)
  @Column(name = "telefone", length = 20)
  private String telefone;
  
  @Size(max = 200)
  @Column(name = "endereco", length = 200)
  private String endereco;
  
  @Size(max = 100)
  @Column(name = "cidade", length = 100)
  private String cidade;
  
  @Size(max = 2)
  @Column(name = "estado", length = 2)
  private String estado;
  
  @Size(max = 9)
  @Column(name = "cep", length = 9)
  private String cep;
  
  @Size(max = 60)
  @Column(name = "pais", length = 60)
  private String pais;
  
  @NotNull
  @Past
  @Column(name = "data_nascimento", nullable = false)
  private LocalDate dataNascimento;
  
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "estado_civil", nullable = false, length = 20)
  private EnumEstadoCivil estadoCivil;
  
  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "genero", nullable = false, length = 20)
  private EnumGenero genero;
  
  @Size(max = 80)
  @Column(name = "profissao", length = 80)
  private String profissao;
  
  @Min(0)
  @Max(10)
  @Column(name = "score", nullable = false)
  private int score;

  // Getters e Setters

  public Cliente() {
    // Construtor padrao exigido pela JPA.
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public void setNomeCompleto(String nomeCompleto) {
    this.nomeCompleto = nomeCompleto;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getPais() {
    return pais;
  }

  public void setPais(String pais) {
    this.pais = pais;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public EnumEstadoCivil getEstadoCivil() {
    return estadoCivil;
  }

  public void setEstadoCivil(EnumEstadoCivil estadoCivil) {
    this.estadoCivil = estadoCivil;
  }

  public EnumGenero getGenero() {
    return genero;
  }

  public void setGenero(EnumGenero genero) {
    this.genero = genero;
  }

  public String getProfissao() {
    return profissao;
  }

  public void setProfissao(String profissao) {
    this.profissao = profissao;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }
}


