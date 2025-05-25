package br.edu.ifpb.academico.Amantes_Cafezinho.configurations;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {

        String errorMessage = "Ocorreu um erro ao tentar fazer login.";

        if (exception instanceof BadCredentialsException) {
            errorMessage = "E-mail ou senha incorretos!";
        } else if (exception instanceof AccountStatusException) {
            errorMessage = "Conta bloqueada. Entre em contato com o suporte!";
        }

        response.sendRedirect("/Amantes_Cafezinho/auth/signin?error=" + errorMessage);
    }
}

