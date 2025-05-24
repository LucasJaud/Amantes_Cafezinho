package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.academico.Amantes_Cafezinho.errors.CafeteriaSemUnidades;
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

    public Unit resgatarUnidadePorId(Long id) {

        return unitService.resgatarUnidadePorId(id);

    }

    public List<Unit> resgatarUnidadesPorCafeteria(Cafeteria cafeteria) throws CafeteriaSemUnidades{

        List<Unit> unidadesResgatadas = unitService.resgatarUnidadesPorCafeteria(cafeteria);

        if (unidadesResgatadas.isEmpty()) {
            throw new CafeteriaSemUnidades();
        }

        return unidadesResgatadas;

    }
    
}
