package br.edu.ifpb.academico.Amantes_Cafezinho.infra;

import br.edu.ifpb.academico.Amantes_Cafezinho.errors.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CafeteriaNotFoundException.class)
    public ModelAndView handleCafeteriaNotFound(CafeteriaNotFoundException ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", ex.getMessage());
        mav.setViewName("error/404");
        return mav;
    }

    @ExceptionHandler(CNPJExistente.class)
    public ModelAndView handlerCNPJExistente(CNPJExistente ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", ex.getMessage());
        mav.setViewName("redirect:/fachada/cadastrarUnidade");
        return mav;
    }

    @ExceptionHandler(ReviewListForReviewerNotFoundException.class)
    public ModelAndView handlerReviewListNotFound(ReviewListForReviewerNotFoundException ex){
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", ex.getMessage());
        mav.setViewName("redirect:/avaliador");
        return mav;
    }

}
