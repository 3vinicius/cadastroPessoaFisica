package br.com.cadastroApi.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ClienteAtualizarDtoTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void deveLancarViolacaoCasoIdSejaNull() {
        ClienteAtualizarDto atualizarClienteDto = new ClienteAtualizarDto(null,"teste", "215233951-76", LocalDate.now(), "teste@gmail.com", "+55 (11) 91234-5678", "57896-741", null);

        Set<ConstraintViolation<ClienteAtualizarDto>> violations = validator.validate( atualizarClienteDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertFalse(violations.isEmpty());
        assertTrue(mensagens.get(0).contains("O ID"));
    }

    @Test
    void naoDeveLancarViolacaoCasoParametrosNulosComIdNaoNulo() {
        ClienteAtualizarDto atualizarClienteDto = new ClienteAtualizarDto(1L,null, null, null, null, null, null, null);

        Set<ConstraintViolation<ClienteAtualizarDto>> violations = validator.validate( atualizarClienteDto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void deveLancarViolacaoCasoCpfInvalido() {
        ClienteAtualizarDto atualizarClienteDto = new ClienteAtualizarDto(1L,"teste", "21523395176", LocalDate.now(), "teste@gmail.com", "+55 (11) 91234-5678", "57896-741", null);

        Set<ConstraintViolation<ClienteAtualizarDto>> violations = validator.validate( atualizarClienteDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertFalse(violations.isEmpty());
        assertTrue(mensagens.get(0).contains("CPF inválido"));
    }

    @Test
    void deveLancarViolacaoCasoEmailInvalido() {
        ClienteAtualizarDto atualizarClienteDto = new ClienteAtualizarDto(1L,"teste", "215233951-76", LocalDate.now(), "testegmail", "+55 (11) 91234-5678", "57896-741", null);

        Set<ConstraintViolation<ClienteAtualizarDto>> violations = validator.validate( atualizarClienteDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertFalse(violations.isEmpty());
        assertTrue(mensagens.get(0).contains("E-mail inválido"));
    }


    @Test
    void deveLancarViolacaoCasoTelefoneInvalido() {
        ClienteAtualizarDto atualizarClienteDto = new ClienteAtualizarDto(1L,"teste", "215233951-76", LocalDate.now(), "teste@gmail.com", "+5 11) 91234-5678", "57896-741", null);

        Set<ConstraintViolation<ClienteAtualizarDto>> violations = validator.validate( atualizarClienteDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertFalse(violations.isEmpty());
        assertTrue(mensagens.get(0).contains("Telefone inválido"));
    }


    @Test
    void deveLancarViolacaoCasoCepInvalido() {
        ClienteAtualizarDto atualizarClienteDto = new ClienteAtualizarDto(1L,"teste", "215233951-76", LocalDate.now(), "teste@gmail.com", "+55 (11) 91234-5678", "57896-71", null);

        Set<ConstraintViolation<ClienteAtualizarDto>> violations = validator.validate( atualizarClienteDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertFalse(violations.isEmpty());
        assertTrue(mensagens.get(0).contains("CEP inválido"));
    }
}