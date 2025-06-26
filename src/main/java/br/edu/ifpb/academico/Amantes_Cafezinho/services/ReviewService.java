package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import br.edu.ifpb.academico.Amantes_Cafezinho.dtos.ReviewListDTO;
import br.edu.ifpb.academico.Amantes_Cafezinho.errors.ReviewListForReviewerNotFoundException;
import br.edu.ifpb.academico.Amantes_Cafezinho.errors.ReviewNotFoundException;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Review;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Reviewer;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Status;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.ReviewRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.StatusRepository;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.UnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private UnitRepository unitRepository;

    // Método auxiliar para recalcular e atualizar a média de uma unidade
    private void updateUnitAverageRating(Unit unit) {
        if (unit != null && unit.getId() != null) {
            Double avg = unitRepository.findAverageRatingByUnitId(unit.getId());
            unit.setAverage(avg != null ? avg : 0.0);
            unitRepository.save(unit);
        }
    }

    @Transactional
    public Review criarReview(Review review) {
        Review reviewGerada = reviewRepository.save(review);
        updateUnitAverageRating(reviewGerada.getUnit());
        return reviewGerada;
    }

    @Transactional
    public void excluirReview(Long id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);

        if (reviewOptional.isPresent()) {
            Review reviewDeletada = reviewOptional.get();
            reviewRepository.deleteById(id);
            updateUnitAverageRating(reviewDeletada.getUnit());
        }
    }

    @Transactional
    public Review atualizarReview(Long id, Review novaAvaliacao){
        Review original = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Avaliação com ID " + id + " não encontrada!"));

        original.update(novaAvaliacao);
        Review reviewAtualizada = reviewRepository.save(original);
        updateUnitAverageRating(reviewAtualizada.getUnit());

        return reviewAtualizada;
    }

    public Review buscarPorId(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public Optional<Status> buscarPorTipo(String tipo) {
        return statusRepository.findByType(tipo);
    }

    public List<Review> listarPorUnidade(Unit unidade) {
        return reviewRepository.findByUnit(unidade);
    }


    public List<ReviewListDTO> listarPorAvaliador (Reviewer reviewer){
        List<Review> reviews = reviewRepository.findByReviewer(reviewer);

        return reviews.stream().map(ReviewListDTO::fromEntity).toList();
    }

//    public void excluirReview(Long id) {
//        if (reviewRepository.existsById(id)) {
//            reviewRepository.deleteById(id);
//        }
//    }
//
//    public void atualizarReview(Long id, Review novaAvaliacao){
//        Review original = reviewRepository.findById(id)
//                .orElseThrow(() -> new ReviewNotFoundException("Avaliação com ID " + id + " não encontrada!"));
//
//        original.update(novaAvaliacao);
//        reviewRepository.save(original);
//    }

}
