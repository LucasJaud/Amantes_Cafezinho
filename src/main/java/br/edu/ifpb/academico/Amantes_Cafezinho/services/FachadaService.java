package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;

@Service
public class FachadaService {

    @Autowired
    private UnitService unitService;
    @Autowired
    private CafeteriaService cafeteriaService;

    public Unit criarUnidade(Unit unidade) {

        unitService.temUnidadeComCnpj(unidade.getCNPJ());
        unitService.cadastrarUnidade(unidade);

        return unidade;

    }

    public Cafeteria resgatarCafeteriaPorCNPJ(String cnpj) {

        return cafeteriaService.resgatarCafeteriaPorCNPJ(cnpj);

    }
    
}
