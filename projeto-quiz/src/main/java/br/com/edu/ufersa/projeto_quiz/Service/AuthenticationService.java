package br.com.edu.ufersa.projeto_quiz.Service;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class AuthenticationService {
    private static final long EXPIRATIONTIME = Duration.ofMinutes(60).toMillis();
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
    public static void addToken(HttpServletResponse res, String email, String role){
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATIONTIME);

        String JwtToken = Jwts.builder()
                .subject(email)
                .claim("role", role) // <--- GRAVA A ROLE NO TOKEN
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(SECRETKEY)
                .compact();

        res.addHeader("Authorization", PREFIX + " " + JwtToken);
        res.addHeader("Access-Control-Expose-Headers", "Authorization");
    }

    public static Authentication getAuthentication (HttpServletRequest request) {
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

                String email = claims.getSubject();
                String role = claims.get("role", String.class);

                if (email != null) {
                    String roleSpring = role.startsWith("ROLE_") ? role : "ROLE_" + role;

                    return new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(new SimpleGrantedAuthority(roleSpring))
                    );
                }
            }
            catch (Exception ex) {
                System.out.println("Erro no token: " + ex.getMessage());
                return null;
            }
        }
        return null;
    }
}


