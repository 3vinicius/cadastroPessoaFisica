package br.com.cadastroApi.services;

import br.com.cadastroApi.exceptions.CepInvalidoException;
import br.com.cadastroApi.services.AutorizacaoService;
import br.com.cadastroApi.services.CepApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;
import static org.junit.jupiter.api.Assertions.*;



class CepApiServiceTest {

    @InjectMocks
    private CepApiService cepService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Description("Deve lançar CepInvalidoException se o cep for inválido")
    void deveLancarCepInvalidoExceptionQuandoCepForInvalido() {
        String cepInvalido = "123";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cepService.validarCep(cepInvalido);
        });

        assertTrue(exception.getMessage().contains("CEP inválido"));
    }

    @Test
    @Description("Deve lançar nenhuma exceção se o cep for válido sem formatação")
    void deveLancarNenhumaExcecaoParaCepValidoSemFormatacao() {
        String cepValido = "57150000";

        assertDoesNotThrow(() -> cepService.validarCep(cepValido));

    }

    @Test
    @Description("Deve lançar CepInvalidoException se o cep for null")
    void deveLancarCepInvalidoExceptionCasoCepNull() {
        String cepInvalido = null;

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cepService.validarCep(cepInvalido);
        });

        assertTrue(exception.getMessage().contains("CEP inválido"));
    }

    @Test
    @Description("Deve lançar nenhuma exceção se o cep for válido")
    void  deveLancarNenhumaExcecaoParaCepValidoFormatado() {
        String cepValido = "57150-000";

        assertDoesNotThrow(() -> cepService.validarCep(cepValido));
    }
}