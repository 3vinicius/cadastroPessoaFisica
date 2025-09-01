package br.com.cadastroApi.mapper;

import br.com.cadastroApi.dto.ClienteAtualizarDto;
import br.com.cadastroApi.dto.ClienteCadastrarDto;
import br.com.cadastroApi.dto.ClienteDto;
import br.com.cadastroApi.model.Cliente;



public class ClienteMapper {

    public static void dtoParaCliente(ClienteAtualizarDto dto, Cliente cliente) {
        if (dto.nome() != null) {
            cliente.setNome(dto.nome());
        }
        if (dto.cpf() != null) {
            cliente.setCpf(dto.cpf());
        }
        if (dto.email() != null) {
            cliente.setEmail(dto.email());
        }
        if (dto.telefone() != null) {
            cliente.setTelefone(dto.telefone());
        }
        if (dto.endereco() != null) {
            cliente.setEndereco(dto.endereco());
        }
        if (dto.cep() != null) {
            cliente.setCep(dto.cep());
        }
        if (dto.dataNascimento() != null) {
            cliente.setDataNascimento(dto.dataNascimento());
        }
    }


    public static void dtoParaCliente(ClienteCadastrarDto dto, Cliente cliente) {
        if (dto.nome() != null) {
            cliente.setNome(dto.nome());
        }
        if (dto.cpf() != null) {
            cliente.setCpf(dto.cpf());
        }
        if (dto.email() != null) {
            cliente.setEmail(dto.email());
        }
        if (dto.telefone() != null) {
            cliente.setTelefone(dto.telefone());
        }
        if (dto.endereco() != null) {
            cliente.setEndereco(dto.endereco());
        }
        if (dto.cep() != null) {
            cliente.setCep(dto.cep());
        }
        if (dto.dataNascimento() != null) {
            cliente.setDataNascimento(dto.dataNascimento());
        }
    }


    public static ClienteDto clienteParaDto(Cliente cliente) {
        return new ClienteDto(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getDataNascimento(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getEndereco(),
                cliente.getCep(),
                cliente.getIdade(),
                cliente.getCreatedAt(),
                cliente.getUpdatedAt()
        );
    }
}