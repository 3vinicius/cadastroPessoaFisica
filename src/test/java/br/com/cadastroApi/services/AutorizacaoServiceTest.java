package br.com.cadastroApi.services;

import br.com.cadastroApi.model.Usuario;
import br.com.cadastroApi.repositorys.UsuarioRepository;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AutorizacaoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;


    @InjectMocks
    private AutorizacaoService autorizacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Deve retornar UserDetails se o email for encontrado quando for logar")
    void deveRetornarUserDetailsQuandoEmailEncontrado() {
        Usuario usuario = new Usuario("test@gmail.com", "password", LocalDateTime.now(), LocalDateTime.now());

        Usuario usuarioEncriptado = usuario;
        usuarioEncriptado.setSenha("passwordHash");

        when(usuarioRepository.findByEmail("test@gmail.com")).thenReturn(usuarioEncriptado);
        
        assertEquals(autorizacaoService.loadUserByUsername("test@gmail.com").getPassword(), usuario.getPassword());

    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando logar com email não cadastrado")
    void deveLancarUsernameNotFoundExceptionQuandoEmailNaoEncontrado() {

        when(autorizacaoService.loadUserByUsername("test@gmail.com")).thenThrow(new UsernameNotFoundException("Usuario não encontrado"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            autorizacaoService.loadUserByUsername("test@gmail.com");
        });

        assertEquals("Usuario não encontrado", exception.getMessage());

    }

    @Test
    @DisplayName("Deve salvar usuario com hash da senha quanto cadastrar um email")
    void deveSalvarUsuarioComHashDaSenha() {
        Usuario usuario = new Usuario("test@gmail.com", "password", LocalDateTime.now(), LocalDateTime.now());



        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);

        when(usuarioRepository.save(usuario)).thenReturn(null);
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(null);
        
        autorizacaoService.cadastrarUsuario(usuario.getEmail(),usuario.getSenha());

        verify(usuarioRepository, times(1)).save(usuarioCaptor.capture());
        Usuario capturado = usuarioCaptor.getValue();
        assertEquals(usuario.getEmail(),capturado.getEmail());
        assertTrue(new BCryptPasswordEncoder().matches(usuario.getPassword(), capturado.getPassword()));



    }

    @Test
    @DisplayName("Deve lançar DuplicateKeyException quanto tentar cadastrar um email já existente")
    void deveLancarDuplicateKeyExceptionQuandoEmailExistir() {
        Usuario usuario = new Usuario("test@gmail.com", "password", LocalDateTime.now(), LocalDateTime.now());



        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(usuario);


        RuntimeException exception = assertThrows(DuplicateKeyException.class, () -> {
            autorizacaoService.cadastrarUsuario(usuario.getEmail(),usuario.getSenha());
        });

        assertEquals(exception.getMessage(), "Email já cadastrado " );

    }


}