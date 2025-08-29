package br.com.cadastroApi.controller;

import br.com.cadastroApi.dto.UsuarioDto;
import br.com.cadastroApi.model.Usuario;
import br.com.cadastroApi.services.AutorizacaoService;
import br.com.cadastroApi.services.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/auth")
public class AutenticacaoController {

    private AuthenticationManager authenticationManager;
    private AutorizacaoService autorizacaoService;
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<String> autenticar(@Valid @RequestBody UsuarioDto usuarioDto) {
        var loginSenha = new UsernamePasswordAuthenticationToken(usuarioDto.email(), usuarioDto.senha());
        var auth = authenticationManager.authenticate(loginSenha);

        String token = tokenService.gerarToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> cadastrar(@Valid @RequestBody UsuarioDto usuarioDto) {
        autorizacaoService.cadastrarUsuario(usuarioDto.email(), usuarioDto.senha());
        return ResponseEntity.ok().build();
    }

}
