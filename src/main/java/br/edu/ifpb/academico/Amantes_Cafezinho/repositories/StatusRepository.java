package br.edu.ifpb.academico.Amantes_Cafezinho.repositories;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findByType(String type);
}
