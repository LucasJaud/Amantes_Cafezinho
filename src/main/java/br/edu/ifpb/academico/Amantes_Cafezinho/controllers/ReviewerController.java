package br.edu.ifpb.academico.Amantes_Cafezinho.controllers;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Review;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Reviewer;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
import br.edu.ifpb.academico.Amantes_Cafezinho.services.ReviewService;
import br.edu.ifpb.academico.Amantes_Cafezinho.services.ReviewerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/avaliador")
@PreAuthorize("hasRole('REVIEWER')")
public class ReviewerController {

    private final ReviewerService reviewerService;
    private final ReviewService reviewService;

    @Autowired
    public ReviewerController(ReviewerService reviewerService, ReviewService reviewService) {
        this.reviewerService = reviewerService;
        this.reviewService = reviewService;
    }


    @GetMapping
    public ModelAndView mostrarPerfil(ModelAndView mav, HttpSession session){
        User user = (User) session.getAttribute( "user");
        Reviewer reviewer = reviewerService.buscarPorUser(user);

        mav.addObject("reviewer", reviewer);
        mav.setViewName("reviewer/perfilAvaliador");

        return mav;

    }

    @DeleteMapping("/deletarAvaliacao/{id}")
    public ModelAndView deletarAvaliacao(ModelAndView mav, HttpSession session, @PathVariable Long id){
        reviewService.deletarAvaliacao(id);
        mav.setViewName("redirect:/avaliador");
        return mav;
    }

    @PutMapping("/atualizarAvaliacao/{id}")
    public ModelAndView atualizarAvaliacao(ModelAndView mav, HttpSession session, @PathVariable Long id, @RequestBody Review novaAvaliacao){
        reviewService.atualizarAvaliacao(id,novaAvaliacao);
        mav.setViewName("redirect:/avaliador");
        return mav;
    }
}
