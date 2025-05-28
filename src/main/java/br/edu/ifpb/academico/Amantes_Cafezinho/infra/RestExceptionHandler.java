package br.edu.ifpb.academico.Amantes_Cafezinho.infra;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.ifpb.academico.Amantes_Cafezinho.errors.CNPJExistente;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{


    
    @ExceptionHandler(CNPJExistente.class)
        public ModelAndView handlerCNPJExistente(CNPJExistente ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", ex.getMessage());
    return new ModelAndView("redirect:/fachada/cadastrarUnidade");
}

    
}
