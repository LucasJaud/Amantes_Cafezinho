package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Review;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Status;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.ReviewRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private StatusRepository statusRepository;

    public Review criarAvaliacao(Review review) {
        return reviewRepository.save(review);
    }

    public Review buscarPorId(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public List<Review> listarPorUnidade(Unit unidade) {
        return reviewRepository.findByUnit(unidade);
    }

    public Status buscarPorTipo(String tipo) {
        return statusRepository.findByType(tipo);
    }
}
