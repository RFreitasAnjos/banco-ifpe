package br.com.ifpe.banco.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.ifpe.banco.entity.Cliente;
import br.com.ifpe.banco.entity.enums.EnumEstadoCivil;
import br.com.ifpe.banco.entity.enums.EnumGenero;
import br.com.ifpe.banco.exception.cliente.ClienteExceptions;
import br.com.ifpe.banco.repository.ClienteRepository;

public class ClienteServiceTests {

  @Mock
  private ClienteRepository clienteRepository;

  @InjectMocks
  private ClienteService clienteService;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void deveSalvarClienteQuandoDadosForemValidos() {
    Cliente cliente = criarCliente(null, "12345678900", "Joao Silva", "joao.silva@example.com");
    when(clienteRepository.buscarPorCpf(cliente.getCpf())).thenReturn(Optional.empty());
    when(clienteRepository.salvar(cliente)).thenReturn(cliente);

    Cliente clienteSalvo = clienteService.salvar(cliente);

    assertSame(cliente, clienteSalvo);
    verify(clienteRepository).buscarPorCpf(cliente.getCpf());
    verify(clienteRepository).salvar(cliente);
  }

  @Test
  public void deveLancarExcecaoAoSalvarQuandoCpfJaExistir() {
    Cliente cliente = criarCliente(null, "12345678900", "Joao Silva", "joao.silva@example.com");
    when(clienteRepository.buscarPorCpf(cliente.getCpf())).thenReturn(Optional.of(criarCliente(99L, cliente.getCpf(), "Outro Cliente", "outro@example.com")));

    assertThrows(ClienteExceptions.class, () -> clienteService.salvar(cliente));

    verify(clienteRepository).buscarPorCpf(cliente.getCpf());
    verify(clienteRepository, never()).salvar(cliente);
  }

  @Test
  public void deveLancarExcecaoAoSalvarQuandoCamposObrigatoriosNaoForemInformados() {
    Cliente cliente = new Cliente();
    cliente.setNomeCompleto(" ");
    cliente.setCpf(null);
    cliente.setEmail(" ");

    assertThrows(ClienteExceptions.class, () -> clienteService.salvar(cliente));

    verify(clienteRepository, never()).buscarPorCpf(org.mockito.ArgumentMatchers.anyString());
    verify(clienteRepository, never()).salvar(org.mockito.ArgumentMatchers.any(Cliente.class));
  }

  @Test
  public void deveLancarExcecaoAoSalvarQuandoEstadoCivilOuGeneroNaoForemInformados() {
    Cliente cliente = criarCliente(null, "12345678900", "Joao Silva", "joao.silva@example.com");
    cliente.setEstadoCivil(null);

    assertThrows(ClienteExceptions.class, () -> clienteService.salvar(cliente));

    verify(clienteRepository, never()).buscarPorCpf(org.mockito.ArgumentMatchers.anyString());
    verify(clienteRepository, never()).salvar(org.mockito.ArgumentMatchers.any(Cliente.class));
  }

  @Test
  public void deveAtualizarClienteQuandoCpfNaoForAlterado() {
    Cliente clienteAtualizado = criarCliente(1L, "12345678900", "Joao Silva", "joao.silva@example.com");
    when(clienteRepository.buscarPorId(1L)).thenReturn(Optional.of(criarCliente(1L, "12345678900", "Joao Antigo", "antigo@example.com")));
    when(clienteRepository.atualizar(clienteAtualizado)).thenReturn(clienteAtualizado);

    Cliente clienteRetornado = clienteService.atualizar(clienteAtualizado);

    assertSame(clienteAtualizado, clienteRetornado);
    verify(clienteRepository).buscarPorId(1L);
    verify(clienteRepository).atualizar(clienteAtualizado);
    verify(clienteRepository, never()).buscarPorCpf(clienteAtualizado.getCpf());
  }

  @Test
  public void deveValidarCpfDuplicadoAoAtualizarQuandoCpfForAlterado() {
    Cliente clienteAtualizado = criarCliente(1L, "99999999999", "Joao Silva", "joao.silva@example.com");
    when(clienteRepository.buscarPorId(1L)).thenReturn(Optional.of(criarCliente(1L, "12345678900", "Joao Silva", "joao.antigo@example.com")));
    when(clienteRepository.buscarPorCpf("99999999999")).thenReturn(Optional.of(criarCliente(2L, "99999999999", "Maria", "maria@example.com")));

    assertThrows(ClienteExceptions.class, () -> clienteService.atualizar(clienteAtualizado));

    verify(clienteRepository).buscarPorId(1L);
    verify(clienteRepository).buscarPorCpf("99999999999");
    verify(clienteRepository, never()).atualizar(clienteAtualizado);
  }

  @Test
  public void deveLancarExcecaoAoAtualizarSemId() {
    Cliente cliente = criarCliente(null, "12345678900", "Joao Silva", "joao.silva@example.com");

    assertThrows(ClienteExceptions.class, () -> clienteService.atualizar(cliente));

    verify(clienteRepository, never()).buscarPorId(org.mockito.ArgumentMatchers.anyLong());
    verify(clienteRepository, never()).atualizar(org.mockito.ArgumentMatchers.any(Cliente.class));
  }

  @Test
  public void deveExcluirClienteEncontradoPorId() {
    Cliente cliente = criarCliente(5L, "12345678900", "Joao Silva", "joao.silva@example.com");
    when(clienteRepository.buscarPorId(5L)).thenReturn(Optional.of(cliente));

    clienteService.excluir(5L);

    verify(clienteRepository).buscarPorId(5L);
    verify(clienteRepository).excluir(cliente);
  }

  @Test
  public void deveListarClientes() {
    List<Cliente> clientes = Arrays.asList(
        criarCliente(1L, "12345678900", "Joao Silva", "joao.silva@example.com"),
        criarCliente(2L, "98765432100", "Maria Souza", "maria.souza@example.com"));
    when(clienteRepository.listarTodos()).thenReturn(clientes);

    List<Cliente> resultado = clienteService.listarClientes();

    assertEquals(clientes, resultado);
    verify(clienteRepository).listarTodos();
  }

  @Test
  public void deveBuscarClientePorId() {
    Cliente cliente = criarCliente(8L, "12345678900", "Joao Silva", "joao.silva@example.com");
    when(clienteRepository.buscarPorId(8L)).thenReturn(Optional.of(cliente));

    Cliente resultado = clienteService.buscarPorId(8L);

    assertSame(cliente, resultado);
    verify(clienteRepository).buscarPorId(8L);
  }

  @Test
  public void deveLancarExcecaoAoBuscarPorIdNulo() {
    assertThrows(ClienteExceptions.class, () -> clienteService.buscarPorId(null));

    verify(clienteRepository, never()).buscarPorId(org.mockito.ArgumentMatchers.anyLong());
  }

  @Test
  public void deveLancarExcecaoAoBuscarClienteInexistente() {
    when(clienteRepository.buscarPorId(77L)).thenReturn(Optional.empty());

    assertThrows(ClienteExceptions.class, () -> clienteService.buscarPorId(77L));

    verify(clienteRepository).buscarPorId(77L);
  }

  private Cliente criarCliente(Long id, String cpf, String nomeCompleto, String email) {
    Cliente cliente = new Cliente();
    cliente.setId(id);
    cliente.setCpf(cpf);
    cliente.setNomeCompleto(nomeCompleto);
    cliente.setEmail(email);
    cliente.setDataNascimento(LocalDate.of(1990, 1, 1));
    cliente.setEstadoCivil(EnumEstadoCivil.SOLTEIRO);
    cliente.setGenero(EnumGenero.MASCULINO);
    cliente.setTelefone("81999999999");
    cliente.setScore(5);
    return cliente;
  }
}
