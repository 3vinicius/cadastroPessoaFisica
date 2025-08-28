package br.com.cadastroApi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record ClienteCadastrarDto(
                                   String nome,

                                   @NotBlank(message = "O CPF é obrigatório")
                                   @Pattern(
                                           regexp = "^\\d{9}-\\d{2}$",
                                           message = "CPF inválido. Formato esperado: 123456789-12"
                                   )
                                   String cpf,
                                   LocalDate dataNascimento,

                                   @Email(message = "E-mail inválido. Formato esperado: teste@gmail.com")
                                   String email,

                                   @NotBlank(message = "O telefone é obrigatório")
                                   @Pattern(
                                           regexp = "^\\+?\\d{2,3}?[- .]?\\(?\\d{2,3}\\)?[- .]?\\d{4,5}[- .]?\\d{4}$",
                                           message = "Telefone inválido. Formato esperado: +55 (11) 91234-5678"
                                   )
                                   String telefone,

                                   @NotBlank(message = "O CEP é obrigatório")
                                   @Pattern(
                                           regexp = "^\\d{5}-\\d{3}$",
                                           message = "CEP inválido. Formato esperado: 12345-678"
                                   )
                                   String cep,
                                   String endereco) {
}
