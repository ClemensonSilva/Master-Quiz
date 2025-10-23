package br.com.edu.ufersa.projeto_quiz.filters;

import br.com.edu.ufersa.projeto_quiz.Service.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class AuthorizationFilter extends GenericFilterBean {
    // intercepta uma requisite e verifica se o token da requisicao é valido. Se for, ele passa
    // para o controller continuar com a requisicao

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Authentication authentication = AuthenticationService.getAuthentication(httpRequest);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        System.out.println("doFilter do AuthorizationFilter");
        filterChain.doFilter(request, response);
    }

}
