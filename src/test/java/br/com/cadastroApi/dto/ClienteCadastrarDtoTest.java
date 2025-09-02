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

class ClienteCadastrarDtoTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void deveLancarViolaoCasoParametrosNulos() {

        ClienteCadastrarDto cadastroDto = new ClienteCadastrarDto(null, null,null, null, null, null, null);

        Set<ConstraintViolation<ClienteCadastrarDto>> violations = validator.validate( cadastroDto);

        assertTrue(violations.size() == 5);

    }

    @Test
    void deveLancarViolaoCasoTelefoneIncorretoSemParentese() {

        ClienteCadastrarDto cadastroDto = new ClienteCadastrarDto("teste", "215233951-76", LocalDate.now(), "teste@gmail.com", "+55 11 91234-5678", "57896-741", null);

        Set<ConstraintViolation<ClienteCadastrarDto>> violations = validator.validate( cadastroDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertFalse(violations.isEmpty());
        assertTrue(mensagens.get(0).contains("Telefone inválido. Formato esperado: +55 (11) 91234-5678"));
    }

    @Test
    void deveLancarViolaoCasoTelefoneIncorretoSemSimbolo() {

        ClienteCadastrarDto cadastroDto = new ClienteCadastrarDto("teste", "215233951-76", LocalDate.now(), "teste@gmail.com", "55 (11) 91234-5678", "57896-741", null);

        Set<ConstraintViolation<ClienteCadastrarDto>> violations = validator.validate( cadastroDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertFalse(violations.isEmpty());
        assertTrue(mensagens.get(0).contains("Telefone inválido. Formato esperado: +55 (11) 91234-5678"));
    }

    @Test
    void deveLancarViolaoCasoCpfIncorretoSemSimbolo() {

        ClienteCadastrarDto cadastroDto = new ClienteCadastrarDto("teste", "21523395176", LocalDate.now(), "teste@gmail.com", "+55 (11) 91234-5678", "57896-741", null);

        Set<ConstraintViolation<ClienteCadastrarDto>> violations = validator.validate( cadastroDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertFalse(violations.isEmpty());
        assertTrue(mensagens.get(0).contains("CPF inválido"));
    }

    @Test
    void deveLancarViolaoCasoEmailIncorreto() {

        ClienteCadastrarDto cadastroDto = new ClienteCadastrarDto("teste", "215233951-76", LocalDate.now(), "testegmail.com", "+55 (11) 91234-5678", "57896-741", null);

        Set<ConstraintViolation<ClienteCadastrarDto>> violations = validator.validate( cadastroDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertFalse(violations.isEmpty());
        assertTrue(mensagens.get(0).contains("E-mail inválido"));
    }

    @Test
    void deveLancarViolaoCasoCepIncorretoSemSimbolo() {

        ClienteCadastrarDto cadastroDto = new ClienteCadastrarDto("teste", "215233951-76", LocalDate.now(), "teste@gmail.com", "+55 (11) 91234-5678", "57896741", null);

        Set<ConstraintViolation<ClienteCadastrarDto>> violations = validator.validate( cadastroDto);

        List<String> mensagens = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        assertFalse(violations.isEmpty());
        assertTrue(mensagens.get(0).contains("CEP inválido"));
    }


    @Test
    void naoDeveLancarViolaoCasoEnderecoNull() {

        ClienteCadastrarDto cadastroDto = new ClienteCadastrarDto("teste", "215233951-76", LocalDate.now(), "teste@gmail.com", "+55 (11) 91234-5678", "57896-741", null);
        Set<ConstraintViolation<ClienteCadastrarDto>> violations = validator.validate( cadastroDto);

        assertTrue(violations.isEmpty());
    }


}