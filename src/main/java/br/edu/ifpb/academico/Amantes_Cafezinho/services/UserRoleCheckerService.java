package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.UserBasedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleCheckerService {

    private final List<UserBasedRepository> repositories;

    @Autowired
    public UserRoleCheckerService(List<UserBasedRepository> repositories) {
        this.repositories = repositories;
    }

    public boolean userExistsInAnyRepository(User user) {
        return repositories.stream()
                .anyMatch(repo -> repo.findByUser(user).isPresent());
    }

    public Optional<Object> findUserInRepositories(User user) {
        return Optional.of(repositories.stream()
                .map(repo -> repo.findByUser(user))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst());
    }
}
