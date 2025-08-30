package br.com.cadastroApi.services;

import br.com.cadastroApi.model.Usuario;
import br.com.cadastroApi.repositorys.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AutorizacaoService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username);
    }

    public void cadastrarUsuario(String email, String senha) {
        if (usuarioRepository.findByEmail(email) != null) {
            throw new DuplicateKeyException("Email já cadastrado " );
        }
        Usuario usuario = new Usuario(email,
                passwordEncoder.encode(senha),
                LocalDateTime.now(),
                LocalDateTime.now());

        usuarioRepository.save(usuario);
    }

}
