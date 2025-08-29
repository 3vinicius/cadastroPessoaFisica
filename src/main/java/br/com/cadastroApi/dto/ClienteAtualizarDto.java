package br.com.cadastroApi.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record ClienteAtualizarDto(
        @NotNull(message = "O ID é obrigatório")
        @Positive(message = "O ID deve ser positivo")
        Long id,
        String nome,

        @CPF
        String cpf,
        LocalDate dataNascimento,

        @Email(message = "E-mail inválido. Formato esperado: teste@gmail.com")
        String email,

        @Pattern(
                regexp = "^\\+?\\d{2,3}?[- .]?\\(?\\d{2,3}\\)?[- .]?\\d{4,5}[- .]?\\d{4}$",
                message = "Telefone inválido. Formato esperado: +55 (11) 91234-5678"
        )
        String telefone,

        @Pattern(
                regexp = "^\\d{5}-\\d{3}$",
                message = "CEP inválido. Formato esperado: 12345-678"
        )
        String cep,
        String endereco) {
}
