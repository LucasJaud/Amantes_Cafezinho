package br.edu.ifpb.academico.Amantes_Cafezinho.repositories;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;

import java.util.Optional;

public interface UserBasedRepository {
    Optional<?> findByUser(User user);
}
