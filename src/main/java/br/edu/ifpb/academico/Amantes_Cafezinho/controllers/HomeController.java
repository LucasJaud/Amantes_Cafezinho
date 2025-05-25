package br.edu.ifpb.academico.Amantes_Cafezinho.controllers;

import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home")
public class HomeController {

    public ModelAndView home (ModelAndView mav, Session session){
        mav.setViewName("home");
        return mav;
    }
}
