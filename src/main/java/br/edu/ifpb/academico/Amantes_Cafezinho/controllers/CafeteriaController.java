package br.edu.ifpb.academico.Amantes_Cafezinho.controllers;

import br.edu.ifpb.academico.Amantes_Cafezinho.errors.UserNotFound;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.CafeteriaRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.UserRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.services.CafeteriaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/cafeteria")
public class CafeteriaController {

    @Autowired
    private CafeteriaService service;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{cnpj}")
    public ModelAndView cafeteriaView(ModelAndView mav, @PathVariable String cnpj, HttpSession session) {
        Cafeteria cafeteria = service.resgatarCafeteriaPorCNPJ(cnpj);
        mav.addObject("cafeteria", cafeteria);
        mav.setViewName("views/showCafeteria");
        return mav;
    }

    public String savePhoto(@PathVariable Long id, @RequestParam MultipartFile file) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFound(id));
        Cafeteria cafeteria = service.findCafeteriaByUser(user);
        service.savePhoto(cafeteria, file);
        return "redirect:/cafeteria/"+cafeteria.getCNPJ();
    }

    @GetMapping("/listarCafeterias")
    public ModelAndView listarCafeterias(ModelAndView mav, @RequestParam(required = false) String nome) {

        if(nome == null || nome.isEmpty()) {
            mav.addObject("cafeterias", service.listarCafeterias());
        } else {
            mav.addObject("cafeterias", service.listarCafeteriasPorNome(nome));
        }

        mav.setViewName("views/listarCafeterias");
        return mav;

    }
}
