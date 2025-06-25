package br.edu.ifpb.academico.Amantes_Cafezinho.configurations;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Admin;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Reviewer;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
import br.edu.ifpb.academico.Amantes_Cafezinho.services.TypeUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    TypeUserService typeUserService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        // Obtém o User autenticado
        User user = (User) authentication.getPrincipal();

        // Cria a sessão
        HttpSession session = request.getSession();

        // Adiciona na sessão
        session.setAttribute("user", user);

        Object typeUser = typeUserService.checkType(user);

        switch (typeUser) {
            case Admin admin -> session.setAttribute("admin", admin);
            case Reviewer reviewer -> session.setAttribute("reviewer", reviewer);
            case Cafeteria cafeteria -> session.setAttribute("cafeteria", cafeteria);
            default -> {throw new RuntimeException("Tipo de usuário não reconhecido!");}
        }
        
        // Redireciona para a página inicial
        response.sendRedirect("/home");
    }
}