package br.edu.ifpb.academico.Amantes_Cafezinho.controllers;

import br.edu.ifpb.academico.Amantes_Cafezinho.dtos.UserFormDTO;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
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

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authservice;

    @PostMapping("/register")
    public ModelAndView register (ModelAndView mav, @Valid @ModelAttribute("UserForm") UserFormDTO userFormDTO, HttpSession session, BindingResult result){
        if (result.hasErrors()) {
            mav.setViewName("auth/signup");
            return mav;
        }
        authservice.register(userFormDTO);
        mav.setViewName("redirect:/auth/signin");
        return mav;
    }

    @GetMapping("/signup")
    public ModelAndView signUp(ModelAndView mav) {
        UserFormDTO userForm = new UserFormDTO();
        mav.addObject("UserForm", userForm);
        mav.setViewName("auth/signup");
        return mav;
    }

    @GetMapping("/signin")
    public ModelAndView signIn(ModelAndView mav) {
        mav.setViewName("auth/signin");
        return mav;
    }

}
