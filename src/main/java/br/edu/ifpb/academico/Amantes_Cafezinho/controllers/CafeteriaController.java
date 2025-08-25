package br.edu.ifpb.academico.Amantes_Cafezinho.controllers;

import br.edu.ifpb.academico.Amantes_Cafezinho.errors.UserNotFound;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.CafeteriaRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.UserRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.services.CafeteriaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/cafeteria")
public class CafeteriaController {

    @Autowired
    private CafeteriaService service;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{cnpj}")
    public ModelAndView cafeteriaView(ModelAndView mav, @PathVariable String cnpj) {
        Cafeteria cafeteria = service.resgatarCafeteriaPorCNPJ(cnpj);

        // Descobre roles do usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isReviewer = auth.getAuthorities().stream()
                .anyMatch(granted -> granted.getAuthority().equals("ROLE_REVIEWER"));

        List<Unit> unidades;
        if (isReviewer) {
            // Apenas unidades ativas
            unidades = cafeteria.getUnits()
                    .stream()
                    .filter(Unit::isActive)
                    .toList();
        } else {
            // Admin ou cafeteria → todas
            unidades = cafeteria.getUnits();
        }

        mav.addObject("cafeteria", cafeteria);
        mav.addObject("units", unidades); // adiciona a lista filtrada
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
    public ModelAndView listarCafeterias(ModelAndView mav,
                                         @RequestParam(required = false) String nome,
                                         @RequestParam(required = false) Double minRating) {

        List<Cafeteria> cafeterias;

        if (nome != null && !nome.isEmpty()) {
            cafeterias = service.listarCafeteriasPorNome(nome);
        } else {
            cafeterias = service.listarCafeterias();
        }

        if (minRating != null) {
            cafeterias = cafeterias.stream()
                    .filter(c -> {
                        List<Unit> units = c.getUnits();
                        if (units.isEmpty()) return false;

                        double averageOfUnits = units.stream()
                                .mapToDouble(Unit::getAverage)
                                .average()
                                .orElse(0.0);

                        return averageOfUnits >= minRating;
                    })
                    .toList();
        }

        mav.addObject("cafeterias", cafeterias);
        mav.setViewName("views/listarCafeterias");
        return mav;
    }
}
