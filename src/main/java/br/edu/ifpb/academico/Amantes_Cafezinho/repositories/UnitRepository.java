package br.edu.ifpb.academico.Amantes_Cafezinho.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface UnitRepository extends JpaRepository<Unit, Long>{

    Optional<Unit> findByCnpj(String cnpj);

    List<Unit> findByCafeteria(Cafeteria cafeteria);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.unit.id = :unitId")
    Optional<Double> findAverageRatingByUnitId(@Param("unitId") Long unitId);

}
