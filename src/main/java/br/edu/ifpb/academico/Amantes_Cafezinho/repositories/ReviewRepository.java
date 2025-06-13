package br.edu.ifpb.academico.Amantes_Cafezinho.repositories;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Review;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUnit(Unit unit);
}
