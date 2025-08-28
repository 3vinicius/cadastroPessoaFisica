package br.com.cadastroApi.repositorys;

import br.com.cadastroApi.model.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByNomeContainingIgnoreCase(String nome);;

    Optional<Cliente> findByCpfContains(String cpf);

    Optional<Cliente> findByEmailContainingIgnoreCase(String email);

    Optional<List<Cliente>> findByEnderecoContainingIgnoreCase(String rua);

    Optional<List<Cliente>> findByIdade(Integer idade);
}