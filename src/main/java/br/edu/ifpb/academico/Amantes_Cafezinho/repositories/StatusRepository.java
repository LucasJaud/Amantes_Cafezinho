package br.edu.ifpb.academico.Amantes_Cafezinho.repositories;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByType(String type);
}
