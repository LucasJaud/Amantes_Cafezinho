package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import br.edu.ifpb.academico.Amantes_Cafezinho.errors.EmailAlreadyInUseException;
import br.edu.ifpb.academico.Amantes_Cafezinho.errors.UsernameAlreadyInUseException;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Reviewer;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Role;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Admin;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    private AdminRepository adminRepository;

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

    public Reviewer loginReviewer(String email, String password) {
        email = email.trim().toLowerCase();
        Reviewer reviewer = reviewerRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        System.out.println(reviewer.getFullName());
        // Verifica bloqueio
        if (reviewer.getAccountLockedUntil() != null &&
                reviewer.getAccountLockedUntil().isAfter(LocalDateTime.now())) {
            throw new RuntimeException(
                    "Conta bloqueada até " + reviewer.getAccountLockedUntil().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );
        }

        // Valida senha
        if (!passwordEncoder.matches(password, reviewer.getUser().getPassword())) {
            reviewer.setFailedLoginAttempts(reviewer.getFailedLoginAttempts() + 1);

            if (reviewer.getFailedLoginAttempts() >= 5) {
                reviewer.setAccountLockedUntil(LocalDateTime.now().plusMinutes(15)); // 15 minutos de bloqueio
                reviewer.setFailedLoginAttempts(0); // reseta após bloquear
            }

            reviewerRepository.save(reviewer);
            System.out.println("CODIGO GERANDO SENHA INCORRETA");
            throw new RuntimeException("Senha incorreta");
        }

        // Login bem-sucedido
        reviewer.setFailedLoginAttempts(0);
        reviewer.setAccountLockedUntil(null);
        reviewerRepository.save(reviewer);

        return reviewer;
    }

    public Cafeteria loginCafeteria(String email, String password) {
        Cafeteria cafeteria = cafeteriaRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (cafeteria.getAccountLockedUntil() != null &&
                cafeteria.getAccountLockedUntil().isAfter(LocalDateTime.now())) {
            throw new RuntimeException(
                    "Conta bloqueada até " + cafeteria.getAccountLockedUntil().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
            );
        }

        if (!passwordEncoder.matches(password, cafeteria.getUser().getPassword())) {
            cafeteria.setFailedLoginAttempts(cafeteria.getFailedLoginAttempts() + 1);

            if (cafeteria.getFailedLoginAttempts() >= 5) {
                cafeteria.setAccountLockedUntil(LocalDateTime.now().plusMinutes(15));
                cafeteria.setFailedLoginAttempts(0);
            }

            cafeteriaRepository.save(cafeteria);
            throw new RuntimeException("Senha incorreta");
        }

        cafeteria.setFailedLoginAttempts(0);
        cafeteria.setAccountLockedUntil(null);
        cafeteriaRepository.save(cafeteria);

        return cafeteria;
    }



    public Admin loginAdmin(String email, String password) {
        email = email.trim().toLowerCase();
        Admin admin = adminRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(password, admin.getUser().getPassword())) {
            throw new RuntimeException("Senha incorreta");
        }

        return admin;
    }

}
