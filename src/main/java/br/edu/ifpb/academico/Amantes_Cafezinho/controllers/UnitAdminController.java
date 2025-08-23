package br.edu.ifpb.academico.Amantes_Cafezinho.controllers;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;
import br.edu.ifpb.academico.Amantes_Cafezinho.services.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin/units")
@PreAuthorize("hasRole('ADMIN')")
public class UnitAdminController {

    @Autowired
    private UnitService unitService;

    @GetMapping
    public ModelAndView listarUnidades(ModelAndView mav) {
        List<Unit> unidades = unitService.listarTodas();
        mav.addObject("unidades", unidades);
        mav.setViewName("admin/controle-unidades");
        return mav;
    }

    @PostMapping("/{id}/toggle")
    public String alterarStatus(@PathVariable Long id) {
        unitService.alterarStatus(id);
        return "redirect:/admin/units";
    }

}
