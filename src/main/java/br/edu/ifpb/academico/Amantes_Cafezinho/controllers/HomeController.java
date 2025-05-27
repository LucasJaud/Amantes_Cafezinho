package br.edu.ifpb.academico.Amantes_Cafezinho.controllers;

import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public ModelAndView home (ModelAndView mav, HttpSession session){
        mav.setViewName("home");
        return mav;
    }
}
