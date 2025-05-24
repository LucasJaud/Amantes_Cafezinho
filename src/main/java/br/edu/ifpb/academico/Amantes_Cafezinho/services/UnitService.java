package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.academico.Amantes_Cafezinho.errors.CNPJExistente;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.CafeteriaRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.UnitRepository;

@Service
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private CafeteriaRepository cafeteriaRepository;

    public boolean temUnidadeComCnpj(String cnpj) throws CNPJExistente{

        if (unitRepository.findByCNPJ(cnpj).isPresent() || cafeteriaRepository.findByCNPJ(cnpj).isPresent()) {
            throw new CNPJExistente();
        }
        
        return true; 
    }

    public void cadastrarUnidade(Unit unidade){

        unitRepository.save(unidade);
        
    }

    public Unit resgatarUnidadePorId(Long id) {
        
        return unitRepository.findById(id).orElse(null);
    }

    public List<Unit> resgatarUnidadesPorCafeteria(Cafeteria cafeteria) {
        
        return unitRepository.findByCafeteria(cafeteria);
    }	








    
}
