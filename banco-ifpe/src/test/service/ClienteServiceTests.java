package test.java.br.com.ifpe.banco.service;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.InjectMocks;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
  public void testCreateCliente(){
    Cliente cliente = new Cliente("12345678900", "Jo?o Silva","joao.silva@example.com");
    when(clienteRepository.save(cliente)).thenReturn(cliente);

    Cliente createdCliente = clienteService.createCliente(cliente);

    assertNotNull(createdCliente);
    assertEquals("12345678900", createdCliente.getCpf());
    assertEquals("Jo?o Silva", createdCliente.getNome());
    assertEquals("joao.silva@example.com", createdCliente.getEmail());

    verify(clienteRepository, times(1)).save(cliente);
  }
}