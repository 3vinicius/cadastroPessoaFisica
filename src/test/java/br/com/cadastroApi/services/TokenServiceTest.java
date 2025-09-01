package br.com.cadastroApi.services;

import br.com.cadastroApi.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Description;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;


class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @Description("Deve retornar um token JWT válido caso tudo ocorra bem")
    void deveRetornarUmTokenCasoTudoCorreto() {

        Usuario usuario = new Usuario("email@gmail.com", "senha", null, null);

        ReflectionTestUtils.setField(tokenService, "secret", "mockedValue");

        assertTrue( tokenService.gerarToken(usuario).contains("Bearer"));

    }


    @Test
    @Description("Deve retornar o email do usuario caso o token JWT seja valido")
    void deveRetornarEmailDeUsuario() {
        Usuario usuario = new Usuario("email@gmail.com", "senha", null, null);

        ReflectionTestUtils.setField(tokenService, "secret", "mockedValue");

        String token = tokenService.gerarToken(usuario).replace("Bearer ", "");

        assertEquals(tokenService.validarToken(token), usuario.getEmail());

    }


    @Test
    @Description("Deve retornar null caso o token seja inválido")
    void deveRetornarNullCasoTokenInvalido() {
        ReflectionTestUtils.setField(tokenService, "secret", "mockedValue");
        String token = "1234";

        assertEquals(tokenService.validarToken(token),null);

    }

}