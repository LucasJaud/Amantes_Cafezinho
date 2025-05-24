package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.CafeteriaRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.UnitRepository;

@Service
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private CafeteriaRepository cafeteriaRepository;

    public boolean temUnidadeComCnpj(String cnpj) {

        unitRepository.findByCNPJ(cnpj).ifPresent(unit -> {
            // lançar um erro
            // implementar amanha
        });

        cafeteriaRepository.findByCNPJ(cnpj).ifPresent(unit -> {
            // lançar um erro
            // implementar amanha 24/05
        });

        return true; 
    }

    public void cadastrarUnidade(Unit unidade){

        unitRepository.save(unidade);
        
    }

    public Unit resgatarUnidadePorId(Long id) {
        
        return unitRepository.findById(id).orElse(null);
    }








    
}
