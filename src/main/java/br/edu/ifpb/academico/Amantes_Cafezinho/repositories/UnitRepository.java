package br.edu.ifpb.academico.Amantes_Cafezinho.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface UnitRepository extends JpaRepository<Unit, Long>{

    Optional<Unit> findByCNPJ(String cnpj);

}
