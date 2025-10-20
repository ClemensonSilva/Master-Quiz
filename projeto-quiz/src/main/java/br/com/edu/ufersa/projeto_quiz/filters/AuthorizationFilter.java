package br.com.edu.ufersa.projeto_quiz.filters;

import br.com.edu.ufersa.projeto_quiz.Service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class AuthorizationFilter extends GenericFilterBean {
    // intercepta uma requisicap e verifica se o token da requisicao é valido. Se for, ele passa
    // para o controller continuar com a requisicao
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("doFilter do AuthorizationFilter");
        Authentication authentication =
                AuthenticationService.getAuthentication((HttpServletRequest)request);
        SecurityContextHolder.getContext().
                setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

}
