package br.com.edu.ufersa.projeto_quiz.filters;

import br.com.edu.ufersa.projeto_quiz.API.dto.LoginDTO;
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
    // pega os dados da requisição e faz a conversao para um objeto LoginDTO
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException, IOException, ServletException {
        LoginDTO credenciais = new ObjectMapper().readValue(req.getInputStream(), LoginDTO.class);
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                credenciais.getEmail(), credenciais.getSenha(), Collections.emptyList()));

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain chain, Authentication auth)
            throws IOException, ServletException {

        org.springframework.security.core.userdetails.UserDetails userData =
                (org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal();

        String email = userData.getUsername();

        String role = userData.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .findFirst()
                .orElse("ALUNO");

        AuthenticationService.addToken(res, email, role);

        System.out.println("successAuthentication do LoginFilter: Login realizado para " + email + " com cargo " + role);
    }

}
