package br.edu.ifpb.academico.Amantes_Cafezinho.services;

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
    public void registerCafeteria(Cafeteria cafeteria) throws RuntimeException{
        User user = validateUser(cafeteria.getUser());
        user.getRoles().add(getRoleByName("cafeteria"));
        cafeteria.setUser(user);
        cafeteriaRepository.save(cafeteria);
    }

    @Transactional
    public void registerReviewer(Reviewer reviewer) throws RuntimeException{
        User user = validateUser(reviewer.getUser());
        user.getRoles().add(getRoleByName("reviewer"));
        reviewer.setUser(user);
        reviewerRepository.save(reviewer);
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

    public User validateUser(User user) throws RuntimeException{
        validateUsernameIsFree(user.getUsername());
        validateEmailIsFree(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    public Role getRoleByName(String name){
        Role role = roleRepository.findByName(name.toUpperCase())
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(name.toUpperCase());
                    return newRole;
                });

        return role;
    }

}
