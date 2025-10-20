package br.com.edu.ufersa.projeto_quiz.Service;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;

@Service
public class AuthenticationService {
    private static final long EXPIRATIONTIME = Duration.ofMinutes(25).toMillis();
    private static  final String  ENV_JWT_KEY = "JWT_SIGNING_KEY_BASE64" ;
    private static final String PREFIX = "Bearer ";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final SecretKey SECRETKEY = loadSecretKey();


    // cria uma chave secreta a partir do JWT_KEY
    private static SecretKey loadSecretKey() {
        Dotenv dotenv = Dotenv.load();
        String base64Key = dotenv.get(ENV_JWT_KEY);
        if (base64Key != null && !base64Key.isBlank()) {
            byte[] keyBytes;
            try {
                keyBytes = Decoders.BASE64.decode(base64Key);
            } catch (IllegalArgumentException e) {
                throw new IllegalStateException("A chave JWT não é uma chave valida na base 64 Base64.", e);
            }
            return Keys.hmacShaKeyFor(keyBytes);
        }
        return null;
    }

    // adiciona o token quando o browser faz a requisição ao servidor
    public static void addToken(HttpServletResponse res, String email){
        System.out.println("addToken do AuthenticationService!");

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATIONTIME);
        String JwtToken = Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(SECRETKEY)
                .compact();

        res.addHeader("Authorization", PREFIX + " " + JwtToken);
        res.addHeader("Access-Control-Expose-Headers", "Authorization");
    }
    // verifica o token após o browser enviar uma requisição ao servidor. Verifica se o token é válido para a requisição
    public static Authentication getAuthentication (HttpServletRequest request) {
        System.out.println("getAuthentication do authenticationService");
        String token = request.getHeader("Authorization");
        if (token != null) {
            if (token.startsWith(PREFIX)) token = token.substring(PREFIX.length()).trim();
            else throw new MalformedJwtException("Invalid Authorization header format");

            try {
                Claims claims = Jwts.parser()
                        .verifyWith(SECRETKEY)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
                String email = claims.get("sub", String.class);
                if (email != null)
                    return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
            }
            catch (ExpiredJwtException ex) {
                System.out.println("Token expirado: " + ex.getMessage());
                return null; // ou lançar exceção customizada
            } catch (JwtException ex) {
                System.out.println("Token inválido: " + ex.getMessage());
                return null;
            }
        }
        return null;
    }
}


