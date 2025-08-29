package br.com.cadastroApi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record UsuarioDto(@NotNull
                         @Size(max = 120)
                         @Email(message = "E-mail inválido. Formato esperado: teste@gmail.com")
                         String email,
                         @NotNull
                         @Size(max = 120)
                         String senha) implements Serializable {
}