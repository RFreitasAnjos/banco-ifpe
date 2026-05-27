package br.com.ifpe.banco.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.ifpe.banco.entity.Cliente;
import br.com.ifpe.banco.exception.cliente.ClienteExceptions;
import br.com.ifpe.banco.repository.ClienteRepository;

@Stateless
public class ClienteService {

  @Inject
  private ClienteRepository clienteRepository;

  public Cliente salvar(Cliente cliente) {
    validarCamposObrigatorios(cliente);

    if (clienteRepository.buscarPorCpf(cliente.getCpf()).isPresent()) {
      throw ClienteExceptions.cpfJaCadastrado(cliente.getCpf());
    }

    return clienteRepository.salvar(cliente);
  }

  public Cliente atualizar(Cliente cliente) {
    validarCamposObrigatorios(cliente);

    if (cliente.getId() == null) {
      throw ClienteExceptions.idObrigatorio();
    }

    Cliente atual = buscarPorId(cliente.getId());

    boolean cpfAlterado = !atual.getCpf().equals(cliente.getCpf());
    if (cpfAlterado && clienteRepository.buscarPorCpf(cliente.getCpf()).isPresent()) {
      throw ClienteExceptions.cpfJaCadastrado(cliente.getCpf());
    }

    return clienteRepository.atualizar(cliente);
  }

  public void excluir(Long id) {
    Cliente cliente = buscarPorId(id);
    clienteRepository.excluir(cliente);
  }

  public List<Cliente> listarClientes() {
    return clienteRepository.listarTodos();
  }

  public Cliente buscarPorId(Long id) {
    if (id == null) {
      throw ClienteExceptions.idObrigatorio();
    }

    return clienteRepository.buscarPorId(id)
        .orElseThrow(() -> ClienteExceptions.clienteNaoEncontrado(id));
  }

  private void validarCamposObrigatorios(Cliente cliente) {
    if (cliente == null
        || isBlank(cliente.getNomeCompleto())
        || isBlank(cliente.getCpf())
        || isBlank(cliente.getEmail())
        || cliente.getDataNascimento() == null) {
      throw ClienteExceptions.dadosObrigatorios();
    }
  }

  private boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}
