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
}
