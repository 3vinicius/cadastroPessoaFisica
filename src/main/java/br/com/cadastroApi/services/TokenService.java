package br.com.cadastroApi.services;

import br.com.cadastroApi.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class TokenService {

    @Value("${token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("cadastro-api")
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(LocalDateTime.now().plusMonths(2).toInstant(Instant.now().atZone(java.time.ZoneId.systemDefault()).getOffset()))
                    .sign(algorithm);

            return "Bearer "+token;
        }catch (JWTCreationException e){
            throw new JWTCreationException("Erro ao gerar token: ", e.getCause());
        }
    }


    public String validarToken(String tokenJWT){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("cadastro-api")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        }catch (JWTVerificationException e){
            return null;
        }
    }
}
