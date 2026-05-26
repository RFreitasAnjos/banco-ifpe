package main.java.br.com.ifpe.banco.constants;

public class PathConstants {
  
  private PathConstants() {
    // Construtor privado para evitar instanciamento
  }

  /** Caminho base da API */
  public static final String BASE_PATH = "/api";
  /** Caminho para recursos de clientes */
  public static final String CLIENTE_PATH = BASE_PATH + "/clientes";
  /** Caminho para recursos de impressoras */
  public static final String PRINTER_PATH = BASE_PATH + "/printer";
  /** Caminho para cancelar proposta */
  public static final String CANCEL_PROPOST_PATH = BASE_PATH + "/cancelPropost";
}
