package br.com.ifpe.banco.exception.cliente;

public class ClienteExceptions extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ClienteExceptions(String message) {
    super(message);
  }

  public static ClienteExceptions cpfJaCadastrado(String cpf) {
    return new ClienteExceptions("Ja existe cliente cadastrado com o CPF: " + cpf);
  }

  public static ClienteExceptions clienteNaoEncontrado(Long id) {
    return new ClienteExceptions("Cliente nao encontrado para o id: " + id);
  }

  public static ClienteExceptions idObrigatorio() {
    return new ClienteExceptions("Id do cliente e obrigatorio para esta operacao.");
  }

  public static ClienteExceptions dadosObrigatorios() {
    return new ClienteExceptions("Nome completo, CPF, email, data de nascimento, estado civil e genero sao obrigatorios.");
  }
}
