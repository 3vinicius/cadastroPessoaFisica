package br.com.cadastroApi.utils;

import br.com.cadastroApi.dto.ClienteAtualizarDto;
import br.com.cadastroApi.dto.ClienteCadastrarDto;
import br.com.cadastroApi.dto.ClienteDto;
import br.com.cadastroApi.mapper.ClienteMapper;
import br.com.cadastroApi.model.Cliente;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClienteTesteUtils {

    public static Cliente gerarCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("teste");
        cliente.setCpf("910641310-26");
        cliente.setDataNascimento(LocalDate.now());
        cliente.setEmail("teste@gmail.com");
        cliente.setTelefone("+55 (11) 91234-5678");
        cliente.setEndereco("rua teste");
        cliente.setCep("57035-003");
        cliente.setIdade(12);
        cliente.setCreatedAt(LocalDateTime.now().minusMonths(5));
        cliente.setUpdatedAt(LocalDateTime.now().minusMonths(2));
        return cliente;
    }

    public static ClienteDto gerarClienteDto() {
        return ClienteMapper.clienteParaDto(gerarCliente());
    }

    public static ClienteAtualizarDto gerarClienteAtualizarDto() {
        Cliente cliente = gerarCliente();
        ClienteDto clienteDto = ClienteMapper.clienteParaDto(cliente);
        return new ClienteAtualizarDto(
                1L, cliente.getNome(), clienteDto.cpf(), cliente.getDataNascimento(),
                cliente.getEmail(), cliente.getTelefone(), cliente.getCep(), cliente.getEndereco()
        );
    }

    public static ClienteCadastrarDto gerarClienteCadastrarDto() {
        Cliente cliente = gerarCliente();
        ClienteDto clienteDto = ClienteMapper.clienteParaDto(cliente);
        return new ClienteCadastrarDto(
                cliente.getNome(), clienteDto.cpf(), cliente.getDataNascimento(),
                cliente.getEmail(), cliente.getTelefone(), cliente.getCep(), cliente.getEndereco()
        );
    }

    public static ClienteCadastrarDto gerarClienteCadastrarDtoInvalido() {
        Cliente cliente = gerarCliente();
        return new ClienteCadastrarDto(
                cliente.getNome(), cliente.getCpf(), cliente.getDataNascimento(),
                cliente.getEmail(), cliente.getTelefone(), cliente.getCep(), cliente.getEndereco()
        );
    }
}