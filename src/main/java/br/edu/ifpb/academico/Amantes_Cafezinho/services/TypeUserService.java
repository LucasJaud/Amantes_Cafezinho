package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import org.springframework.beans.factory.annotation.Autowired;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.AdminRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.CafeteriaRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.ReviewerRepository;

public class TypeUserService {
    @Autowired
    AdminRepository adminRepository;

    @Autowired
    CafeteriaRepository cafeteriaRepository;

    @Autowired
    ReviewerRepository reviewerRepository;

    public Object checkType(User user) {
        return adminRepository.findByUser(user)
            .map(admin -> (Object) admin)
            .or(() -> cafeteriaRepository.findByUser(user).map(cafeteria -> (Object) cafeteria))
            .or(() -> reviewerRepository.findByUser(user).map(reviewer -> (Object) reviewer))
            .orElse(null);
    }
    
}
