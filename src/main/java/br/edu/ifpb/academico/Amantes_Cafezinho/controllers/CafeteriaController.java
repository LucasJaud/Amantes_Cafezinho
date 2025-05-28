package br.edu.ifpb.academico.Amantes_Cafezinho.controllers;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.CafeteriaRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.services.CafeteriaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/cafeteria")
public class CafeteriaController {

    @Autowired
    private CafeteriaService service;

    @GetMapping("/{cnpj}")
    public ModelAndView cafeteriaView(ModelAndView mav,@PathVariable String cnpj) {
        Cafeteria cafeteria = service.resgatarCafeteriaPorCNPJ(cnpj);
        mav.addObject("cafeteria", cafeteria);
        mav.setViewName("views/showCafeteria");
        return mav;
    }
}
