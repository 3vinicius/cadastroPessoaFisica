package br.com.cadastroApi.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ClienteAtualizarDtoTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    @Order(1)
    void deveLancarViolacaoCasoIdSejaNull() {
        ClienteAtualizarDto atualizarClienteDto = new ClienteAtualizarDto(null,"teste", "215233951-76", LocalDate.now(), "teste@gmail.com", "+55 (11) 91234-5678", "57896-741", null);

        Set<ConstraintViolation<ClienteAtualizarDto>> violations = validator.validate( atualizarClienteDto);

        assertFalse(violations.isEmpty());
    }

    @Test
    @Order(2)
    void naoDeveLancarViolacaoCasoParametrosNulosComIdNaoNulo() {
        ClienteAtualizarDto atualizarClienteDto = new ClienteAtualizarDto(1L,null, null, null, null, null, null, null);

        Set<ConstraintViolation<ClienteAtualizarDto>> violations = validator.validate( atualizarClienteDto);

        assertTrue(violations.isEmpty());
    }

    @Test
    @Order(3)
    void deveLancarViolacaoCasoCpfInvalido() {
        ClienteAtualizarDto atualizarClienteDto = new ClienteAtualizarDto(1L,"teste", "21523395176", LocalDate.now(), "teste@gmail.com", "+55 (11) 91234-5678", "57896-741", null);

        Set<ConstraintViolation<ClienteAtualizarDto>> violations = validator.validate( atualizarClienteDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        boolean temErro = mensagens.stream()
                .anyMatch(msg -> msg.contains("CPF inválido"));

        assertTrue(temErro);
    }

    @Test
    @Order(4)
    void deveLancarViolacaoCasoEmailInvalido() {
        ClienteAtualizarDto atualizarClienteDto = new ClienteAtualizarDto(1L,"teste", "215233951-76", LocalDate.now(), "testegmail", "+55 (11) 91234-5678", "57896-741", null);

        Set<ConstraintViolation<ClienteAtualizarDto>> violations = validator.validate( atualizarClienteDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());



        boolean temErroDeEmail = mensagens.stream()
                .anyMatch(msg -> msg.contains("E-mail inválido"));

        assertTrue(temErroDeEmail);
    }


    @Test
    @Order(5)
    void deveLancarViolacaoCasoTelefoneInvalido() {
        ClienteAtualizarDto atualizarClienteDto = new ClienteAtualizarDto(1L,"teste", "215233951-76", LocalDate.now(), "teste@gmail.com", "+5 11) 91234-5678", "57896-741", null);

        Set<ConstraintViolation<ClienteAtualizarDto>> violations = validator.validate( atualizarClienteDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());


        boolean temErroDeEmail = mensagens.stream()
                .anyMatch(msg -> msg.contains("Telefone inválido"));

        assertTrue(temErroDeEmail);

        assertFalse(violations.isEmpty());

    }


    @Test
    @Order(6)
    void deveLancarViolacaoCasoCepInvalido() {
        ClienteAtualizarDto atualizarClienteDto = new ClienteAtualizarDto(1L,"teste", "215233951-76", LocalDate.now(), "teste@gmail.com", "+55 (11) 91234-5678", "57896-71", null);

        Set<ConstraintViolation<ClienteAtualizarDto>> violations = validator.validate( atualizarClienteDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertFalse(violations.isEmpty());
        boolean temErro = mensagens.stream()
                .anyMatch(msg -> msg.contains("CEP inválido"));

        assertTrue(temErro);
    }
}