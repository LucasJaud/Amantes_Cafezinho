package br.edu.ifpb.academico.Amantes_Cafezinho.configurations;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Admin;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Role;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.AdminRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.RoleRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("REVIEWER");
        createRoleIfNotExists("CAFETERIA");
        createAdminIfNotExists();
    }

    private void createRoleIfNotExists(String roleName) {
        roleRepository
                .findByName(roleName)
                .orElseGet(
                        () -> {
                            Role role = new Role();
                            role.setName(roleName);
                            return roleRepository.save(role);
                        });
    }

    public void createAdminIfNotExists(){
        if(userRepository.findByEmail("admin@amantes.cafezinho.com").isEmpty()){
            User user = new User();
            user.setEmail("admin@amantes.cafezinho.com");
            user.setPassword(passwordEncoder.encode("admin1234"));
            user.setUsername("ADMIN");
            user.setCreationDate(LocalDateTime.now());
            Role role = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("ADMIN");
                        return newRole;
                    });
            user.getRoles().add(role);
            Admin admin = new Admin();
            admin.setUser(user);
            admin.setPermissionApproval(true);
            adminRepository.save(admin);
        }
    }
}

