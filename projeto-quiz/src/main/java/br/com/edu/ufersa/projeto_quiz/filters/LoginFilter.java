package br.com.edu.ufersa.projeto_quiz.filters;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Usuario;
import br.com.edu.ufersa.projeto_quiz.Service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.util.Collections;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {
    // requisição para login. Vai analisar a url e o método POST
    public LoginFilter(String url, AuthenticationManager authenticationManager) {
        super(req -> req.getServletPath().equals(url) && "POST".equalsIgnoreCase(req.getMethod()) );
        setAuthenticationManager(authenticationManager);
    }
    // pega os dados da requisição e faz a conversao para um objeto Usuario
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException, IOException, ServletException {
            Usuario usuario = new ObjectMapper().readValue(req.getInputStream(), Usuario.class);
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                usuario.getEmail(), usuario.getSenha(), Collections.emptyList()));

    }

    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication auth)
            throws IOException, ServletException {
        AuthenticationService.addToken(res, auth.getName());
        System.out.println("successAuthentication do LoginFilter");
    }

}
