package br.edu.ifpb.academico.Amantes_Cafezinho.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;
import br.edu.ifpb.academico.Amantes_Cafezinho.services.FachadaService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("fachada")
public class FachadaController {

    @Autowired
    private FachadaService fachadaService;

    @PostMapping(("/criarUnidade"))
    public ModelAndView criarUnidade
                                    (ModelAndView mav,
                                    @Validated @ModelAttribute("Unit") Unit unidade,
                                    BindingResult result, 
                                    HttpSession session,  
                                    RedirectAttributes redirectAttributes){

        // verifica se o objeto unidade é valido
        // se não for, redireciona para a página de cadastro
        if (result.hasErrors()) {
             mav.setViewName("redirect:/cadastrUnidade");
        }

        // verifica se existe unidade com o mesmo CNPJ, caso não, cria a unidade e resgata o id
        Long IDdaUnidadeCriada = fachadaService.criarUnidade(unidade).getId();

        // resgata o CNPJ da cafeteria logada para atualizar o atributo na sessão
        String CNPJdaCafeteriaLogada = ((Cafeteria) session.getAttribute("CafeteriaLogada")).getCNPJ();
        Cafeteria CafeteriaLogada = fachadaService.resgatarCafeteriaPorCNPJ(CNPJdaCafeteriaLogada);
        session.setAttribute("CafeteriaLogada", CafeteriaLogada);

        // adiciona mensagem de sucesso
        // e redireciona para o perfil da unidade criada
        // ñ está implementado ainda o profile da unidade
        redirectAttributes.addFlashAttribute("success", "Unidade criada com sucesso!");
        mav.setViewName("redirect:/profileUnidade/" + IDdaUnidadeCriada);

        return mav;
        
    }

}
