package main.java.br.com.ifpe.banco.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Table;

import main.java.br.com.ifpe.banco.entity.enums.EnumEstadoCivil;
import main.java.br.com.ifpe.banco.entity.enums.EnumGenero;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;
  private String nomeCompleto;
  private String cpf;
  private String email;
  private String telefone;
  private String endereco;
  private String cidade;
  private String estado;
  private String cep;
  private String pais;
  private LocalDate dataNascimento;
  private EnumEstadoCivil estadoCivil;
  private EnumGenero genero;
  private String profissao;
  private int score;

  // Getters e Setters
  // Implementar posteriormente
}
