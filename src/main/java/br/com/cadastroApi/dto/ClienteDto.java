package br.com.cadastroApi.dto;

import br.com.cadastroApi.model.Cliente;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClienteDto(Long id,
                         String nome,
                         String cpf,
                         LocalDate dataNascimento,
                         String email,
                         String telefone,
                         String endereco,
                         String cep,
                         Integer idade,
                         LocalDateTime createdAt,
                         LocalDateTime updatedAt) {
    public ClienteDto(Cliente cliente) {

        this(cliente.getId(), cliente.getNome(), cliente.getCpf(), cliente.getDataNascimento(),
                cliente.getEmail(), cliente.getTelefone(), cliente.getEndereco(), cliente.getCep(),
                cliente.getIdade() , cliente.getCreatedAt(), cliente.getUpdatedAt());
    }
}
