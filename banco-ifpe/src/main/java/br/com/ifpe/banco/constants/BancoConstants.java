package br.com.ifpe.banco.constants;

public class BancoConstants {
  private BancoConstants() {
    // Construtor privado para evitar instanciamento
  }

  /** Nome do banco */
  public static final String NOME_BANCO = "Banco IFPE";
  /** CNPJ do banco */
  public static final String CNPJ_BANCO = "12.345.678/0001-90";
  /** Endere?o do banco */
  public static final String ENDERECO_BANCO = "Rua Bar?o de Lucena, 123 - Jaboat?o dos Guararapes, PE";

  public static final String AGENCIA_PADRAO = "0001";
  /** Valor m?nimo para dep?sito */
  public static final double MIN_DEPOSITO = 10.0;
  /** Valor m?nimo para saque */
  public static final double MIN_SAQUE = 20.0;
}
