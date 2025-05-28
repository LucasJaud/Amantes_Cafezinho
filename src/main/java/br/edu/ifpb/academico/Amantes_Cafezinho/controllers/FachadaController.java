package br.edu.ifpb.academico.Amantes_Cafezinho.controllers;

import java.util.Enumeration;
import java.time.LocalDate;
import java.util.List;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.*;
import br.edu.ifpb.academico.Amantes_Cafezinho.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import br.edu.ifpb.academico.Amantes_Cafezinho.services.FachadaService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("fachada")
public class FachadaController {

    @Autowired
    private FachadaService fachadaService;
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/criarUnidade")
    public ModelAndView criarUnidade
                                    (ModelAndView mav,
                                    @Validated @ModelAttribute("unit") Unit unidade,
                                    BindingResult result, 
                                    HttpSession session,  
                                    RedirectAttributes redirectAttributes){

        // verifica se o objeto unidade é valido
        // se não for, redireciona para a página de cadastro
        if (result.hasErrors()) {
             mav.setViewName("views/cadastrarUnidade");
             return mav;
        }

        // verifica se existe unidade com o mesmo CNPJ, caso não, cria a unidade e resgata o id
        Long IDdaUnidadeCriada = fachadaService.criarUnidade(unidade).getId();

        // resgata o CNPJ da cafeteria logada para atualizar o atributo na sessão
        String CNPJdaCafeteriaLogada = ((Cafeteria) session.getAttribute("cafeteria")).getCNPJ();
        Cafeteria CafeteriaLogada = fachadaService.resgatarCafeteriaPorCNPJ(CNPJdaCafeteriaLogada);
        session.setAttribute("cafeteria", CafeteriaLogada);

        // adiciona mensagem de sucesso
        // e redireciona para o perfil da unidade criada
        // ñ está implementado ainda o profile da unidade
        redirectAttributes.addFlashAttribute("success", "Unidade criada com sucesso!");
        mav.setViewName("redirect:/fachada/perfilUnidade/" + IDdaUnidadeCriada);

        return mav;
        
    }


    @GetMapping("/perfilUnidade/{id}")
    public ModelAndView perfilUnidade(ModelAndView mav, @PathVariable Long id) {
        Unit unidade = fachadaService.resgatarUnidadePorId(id);
        List<Review> avaliacoes = unidade.getReviews();
        mav.addObject("unidadeEscolhida", unidade);
        mav.addObject("avaliacoes", avaliacoes);
        mav.setViewName("views/perfilUnidade");
        
        return mav;
    }


    @GetMapping("/listarUnidades")
    public ModelAndView listarUnidades(ModelAndView mav, HttpSession session){
        // Resgata a cafeteria logada
        Cafeteria cafeteriaLogada =(Cafeteria) session.getAttribute("cafeteria");
        
        // Resgata todas as unidades da cafeteria logada
        List<Unit> unidades = fachadaService.resgatarUnidadesPorCafeteria(cafeteriaLogada);
        
        // Adiciona as unidades ao modelo
        mav.addObject("unidades", unidades);
        
        // Define a view para listar as unidades
        mav.setViewName("views/listarUnidades");
        
        return mav;
    }


    @GetMapping("/cadastrarUnidade")
    public ModelAndView cadastrUnidade(ModelAndView mav, Unit unidade, HttpSession session) {

        Cafeteria cafeteria = (Cafeteria) session.getAttribute("cafeteria");
        unidade.setCafeteria(cafeteria);
        mav.addObject("Unidade", unidade);
        mav.setViewName("views/cadastrarUnidade");

        return mav;
    }

    @PostMapping("/perfilUnidade/{unitId}/criarAvaliacao")
    public ModelAndView criarAvaliacao(
            ModelAndView mav,
            @Validated @ModelAttribute("Review") Review review,
            BindingResult result,
            @PathVariable Long unitId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao criar avaliação. Verifique os dados.");
            mav.setViewName("redirect:/fachada/perfilUnidade/" + unitId);

            return mav;
        }

        Unit unidade = fachadaService.resgatarUnidadePorId(unitId);

        if (unidade == null) {
            redirectAttributes.addFlashAttribute("error", "Unidade não encontrada.");
            mav.setViewName("redirect:/fachada/listarUnidades");

            return mav;
        }

        Reviewer loggedReviewer = fachadaService.buscarReviewerPorUser((User) session.getAttribute("user"));
        if (loggedReviewer == null) {
            redirectAttributes.addFlashAttribute("error", "Você precisa estar logado como avaliador para avaliar.");
            mav.setViewName("redirect:/login");
            return mav;
        }

        review.setUnit(unidade);
        review.setReviewer(loggedReviewer);
        review.setDatetime(LocalDate.now());
        review.setStatus(reviewService.buscarPorTipo("ativo"));
        fachadaService.criarAvaliacao(review);
        redirectAttributes.addFlashAttribute("success", "Avaliação criada com sucesso!");
        mav.setViewName("redirect:/fachada/perfilUnidade/" + unitId);

        return mav;
    }

    @GetMapping("/perfilUnidade/{unitId}/criarAvaliacao")
    public ModelAndView getFormularioAvaliacao(
            ModelAndView mav,
            @PathVariable Long unitId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Reviewer loggedReviewer = fachadaService.buscarReviewerPorUser((User) session.getAttribute("user"));
        if (loggedReviewer == null) {
            redirectAttributes.addFlashAttribute("error", "Você precisa estar logado como avaliador para avaliar.");
            mav.setViewName("redirect:/login");
            return mav;
        }

        Unit unidade = fachadaService.resgatarUnidadePorId(unitId);
        if (unidade == null) {
            redirectAttributes.addFlashAttribute("error", "Unidade não encontrada.");
            mav.setViewName("redirect:/fachada/listarUnidades");
            return mav;
        }

        mav.addObject("Review", new Review());
        mav.addObject("unidadeEscolhida", unidade);
        mav.setViewName("review/formulario-avaliacao");

        return mav;
    }

}
