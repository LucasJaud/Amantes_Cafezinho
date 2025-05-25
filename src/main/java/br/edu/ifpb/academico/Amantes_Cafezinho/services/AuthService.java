package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import br.edu.ifpb.academico.Amantes_Cafezinho.dtos.UserFormDTO;
import br.edu.ifpb.academico.Amantes_Cafezinho.errors.EmailAlreadyInUseException;
import br.edu.ifpb.academico.Amantes_Cafezinho.errors.UsernameAlreadyInUseException;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Reviewer;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Role;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.CafeteriaRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.ReviewerRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.RoleRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CafeteriaRepository cafeteriaRepository;

    @Autowired
    private ReviewerRepository reviewerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserFormDTO dto) throws RuntimeException{
        User user = new User();

        validateEmailIsFree(dto.email());
        validateUsernameIsFree(dto.username());

        user.setEmail(dto.email());
        user.setUsername(dto.username());

        user.setPassword(passwordEncoder.encode(dto.password()));

        if(dto.userType().contains("REVIEWER")){
            Reviewer reviewer = new Reviewer();
            Role role = roleRepository.findByName(dto.userType())
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName(dto.userType());
                        return newRole;
                    });

            user.getRoles().add(role);
            reviewer.setUser(user);
            reviewer.setCPF(dto.cpf());
            reviewer.setBirthDate(dto.birthDate());
            reviewer.setSex(dto.sex());
            reviewer.setFullName(dto.fullName());
            reviewerRepository.save(reviewer); // criar service para validar os campos únicos
        }

        if(dto.userType().contains("CAFETERIA")) {
            Cafeteria cafeteria = new Cafeteria();
            Role role = roleRepository.findByName(dto.userType())
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName(dto.userType());
                        return newRole;
                    });

            user.getRoles().add(role);
            cafeteria.setUser(user);
            cafeteria.setCNPJ(dto.cnpj());
            cafeteria.setSocialReason(dto.socialReason());
            cafeteria.setFantasyName(dto.fantasyName());
            cafeteriaRepository.save(cafeteria); // criar service para validar os campos únicos
        }

    }

    public void validateEmailIsFree(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyInUseException(email);
        }
    }

    public void validateUsernameIsFree(String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyInUseException(username);
        }
    }

}
