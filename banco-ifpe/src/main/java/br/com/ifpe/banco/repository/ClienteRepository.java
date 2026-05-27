package br.com.ifpe.banco.repository;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.ifpe.banco.entity.Cliente;

@Stateless
public class ClienteRepository {

  @PersistenceContext(unitName = "ClientePU")
  private EntityManager entityManager;

  public Cliente salvar(Cliente cliente) {
    entityManager.persist(cliente);
    return cliente;
  }

  public Cliente atualizar(Cliente cliente) {
    return entityManager.merge(cliente);
  }

  public void excluir(Cliente cliente) {
    entityManager.remove(entityManager.contains(cliente) ? cliente : entityManager.merge(cliente));
  }

  public Optional<Cliente> buscarPorId(Long id) {
    return Optional.ofNullable(entityManager.find(Cliente.class, id));
  }

  public Optional<Cliente> buscarPorCpf(String cpf) {
    List<Cliente> result = entityManager
        .createQuery("SELECT c FROM Cliente c WHERE c.cpf = :cpf", Cliente.class)
        .setParameter("cpf", cpf)
        .setMaxResults(1)
        .getResultList();

    return result.stream().findFirst();
  }

  public Optional<Cliente> buscarPorNomeCompleto(String nomeCompleto) {
    List<Cliente> result = entityManager
        .createQuery("SELECT c FROM Cliente c WHERE c.nomeCompleto = :nomeCompleto", Cliente.class)
        .setParameter("nomeCompleto", nomeCompleto)
        .setMaxResults(1)
        .getResultList();

    return result.stream().findFirst();
  }

  public List<Cliente> listarTodos() {
    return entityManager
        .createQuery("SELECT c FROM Cliente c ORDER BY c.nomeCompleto", Cliente.class)
        .getResultList();
  }
}
