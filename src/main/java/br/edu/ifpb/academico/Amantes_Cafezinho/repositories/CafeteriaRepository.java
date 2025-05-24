package br.edu.ifpb.academico.Amantes_Cafezinho.repositories;
import java.util.List;
import java.util.Optional;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Cafeteria;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CafeteriaRepository  extends JpaRepository<Cafeteria, Long> {

     Optional<Cafeteria> findByCNPJ(String cnpj);

}
