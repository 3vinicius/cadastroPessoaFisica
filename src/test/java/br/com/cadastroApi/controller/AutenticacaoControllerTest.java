package br.com.cadastroApi.controller;

import br.com.cadastroApi.dto.UsuarioDto;
import br.com.cadastroApi.model.Usuario;
import br.com.cadastroApi.repositorys.UsuarioRepository;
import br.com.cadastroApi.services.AutorizacaoService;
import br.com.cadastroApi.services.TokenService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "token.secret=teste123"
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AutenticacaoControllerTest {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AutorizacaoService autorizacaoService;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private AutenticacaoController autenticacaoController;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private UsuarioDto usuarioDto = new UsuarioDto("teste@gmail.com", "senha123");


    @Test
    @Order(1)
    void deveCadastrarUsuario() {

        autenticacaoController.cadastrar(usuarioDto);

        assertTrue(usuarioRepository.findByEmail("teste@gmail.com") != null);
    }

    @Test
    @Order(2)
    void deveRetornarTokenAoLogar() {

        Usuario usuario = new Usuario();
        usuario.setSenha("senha123");
        usuario.setEmail("teste@gmail.com");

        ResponseEntity<String> response = autenticacaoController.autenticar(usuarioDto);

        assertTrue(response.getBody().contains("Bearer "));
    }

    @Test
    @Order(3)
    @Transactional
    @Commit
    void deveDeletarUsuarioAposTeste() {
        usuarioRepository.deleteByEmail("teste@gmail.com");

        assertTrue(usuarioRepository.findByEmail("teste@gmail.com") == null);

    }

}