package br.edu.ifpb.academico.Amantes_Cafezinho.controllers;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Reviewer;
import br.edu.ifpb.academico.Amantes_Cafezinho.services.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authservice;

    @GetMapping("/login")
    public ModelAndView logIn(ModelAndView mav) {
        mav.setViewName("auth/login");
        return mav;
    }

    @PostMapping("/process-login")
    public ModelAndView logInPost(String email, String password, ModelAndView mav, HttpSession session) {
        try {
            System.out.println("teste22");

            try {
                Reviewer reviewer = authservice.loginReviewer(email, password);

                // Autenticar no Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(reviewer, null, reviewer.getUser().getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Salvar contexto de segurança na sessão
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext());

                session.setAttribute("role", "reviewer");
                mav.setViewName("redirect:/home");
                return mav;

            } catch (RuntimeException e) {
                if (!"Usuário não encontrado".equals(e.getMessage())) {
                    throw e; // se for outro erro, não tenta cafeteria
                }

                Cafeteria cafeteria = authservice.loginCafeteria(email, password);

                // Autenticar no Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(cafeteria, null, cafeteria.getUser().getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // Salvar contexto de segurança na sessão
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                        SecurityContextHolder.getContext());

                session.setAttribute("role", "cafeteria");
                mav.setViewName("redirect:/home");
                return mav;
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            mav.addObject("error", e.getMessage());
            mav.setViewName("auth/login");
            return mav;
        }
    }

    @GetMapping("/signup")
    public ModelAndView signUp(ModelAndView mav) {
        mav.setViewName("auth/signup");
        return mav;
    }

    @GetMapping("/signup/cafeteria")
    public ModelAndView signUpCafeteria(ModelAndView mav, Cafeteria cafeteria) {
        mav.addObject("cafeteria", cafeteria);
        mav.setViewName("auth/signup-cafeteria");
        return mav;
    }

    @GetMapping("/signup/reviewer")
    public ModelAndView signUpReviewer(ModelAndView mav, Reviewer reviewer) {
        mav.addObject("reviewer", reviewer);
        mav.setViewName("auth/signup-reviewer");
        return mav;
    }

    @PostMapping("/save/cafeteria")
    public ModelAndView register (ModelAndView mav, @Valid @ModelAttribute("cafeteria") Cafeteria cafeteria, BindingResult result){
        if (result.hasErrors()) {
            mav.setViewName("auth/signup-cafeteria");
            return mav;
        }
        authservice.registerCafeteria(cafeteria);
        mav.setViewName("redirect:/auth/login");
        return mav;
    }

    @PostMapping("/save/reviewer")
    public ModelAndView register (ModelAndView mav, @Valid @ModelAttribute("reviewer") Reviewer reviewer, BindingResult result){
        if (result.hasErrors()) {
            mav.setViewName("auth/signup-reviewer");
            return mav;
        }
        authservice.registerReviewer(reviewer);
        mav.setViewName("redirect:/auth/login");
        return mav;
    }

}
