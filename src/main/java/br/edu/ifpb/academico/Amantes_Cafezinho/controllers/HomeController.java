package br.edu.ifpb.academico.Amantes_Cafezinho.controllers;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Admin;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Reviewer;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.CafeteriaRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.ReviewerRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.services.CafeteriaService;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private CafeteriaRepository cafeteriaRepository;

    @Autowired
    private ReviewerRepository reviewerRepository;

    @GetMapping
    public ModelAndView home (ModelAndView mav, HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user != null){
            Cafeteria cafeteria = cafeteriaRepository.findByUser(user).orElse(null);
            System.out.println(user);
            System.out.println(cafeteria);
            mav.addObject("user", user);
            if(cafeteria != null){
                mav.addObject("cafeteria", cafeteria);
            }
        }
        mav.setViewName("home");
        return mav;
    }
}
