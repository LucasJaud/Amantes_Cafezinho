package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.CafeteriaRepository;

@Service
public class CafeteriaService {

    @Autowired
    private CafeteriaRepository cafeteriaRepository;

    public Cafeteria resgatarCafeteriaPorCNPJ(String cnpj) {

        return cafeteriaRepository.findByCNPJ(cnpj).get();

    }
    
}
