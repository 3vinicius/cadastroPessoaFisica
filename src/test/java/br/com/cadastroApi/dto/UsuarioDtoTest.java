package br.com.cadastroApi.dto;

import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;


import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDtoTest {


    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @Test
    void deveLancarViolacaoCasoEmailSenhaNull() {

        UsuarioDto usuarioDto = new UsuarioDto(null,null);

        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate( usuarioDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertFalse(violations.isEmpty());
        assertTrue(mensagens.get(0).contains("não deve ser nulo"));
        assertTrue(mensagens.get(1).contains("não deve ser nulo"));

    }

    @Test
    void deveLancarViolacaoCasoEmailInvalido() {

        UsuarioDto usuarioDto = new UsuarioDto("teste","senha123");

        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate( usuarioDto);


        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertFalse(violations.isEmpty());
        assertTrue(mensagens.get(0).contains("E-mail inválido"));

    }

    @Test
    void naoDeveLancarViolacaoCasoTudoValido() {

        UsuarioDto usuarioDto = new UsuarioDto("teste@gmail.com","senha123");

        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate( usuarioDto);

        assertTrue(violations.isEmpty());


    }

}