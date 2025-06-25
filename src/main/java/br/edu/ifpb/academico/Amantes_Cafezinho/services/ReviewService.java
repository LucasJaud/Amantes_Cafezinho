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
    private void atualizarMediaAvaliacaoUnidade(Unit unit) {
        Optional<Double> avg = unitRepository.findAverageRatingByUnitId(unit.getId());
        unit.setAverage(avg.orElse(0.0));
        unitRepository.save(unit);
    }

    @Transactional
    public Review criarReview(Review review) {
        Review reviewGerada = reviewRepository.save(review);
        Unit unidade = reviewGerada.getUnit();
        if (unidade.getId() == null) {
            unidade = unitRepository.save(unidade);
        } else {
            unidade = unitRepository.findById(unidade.getId()).orElse(unidade);
        }

        atualizarMediaAvaliacaoUnidade(unidade);

        return reviewGerada;
    }

    @Transactional
    public void excluirReview(Long id) {
        Optional<Review> reviewDeletadaOptional = reviewRepository.findById(id);

        if (reviewDeletadaOptional.isPresent()) {
            Review reviewDeletada = reviewDeletadaOptional.get();
            reviewRepository.deleteById(id);

            Unit unidade = unitRepository.findById(reviewDeletada.getUnit().getId())
                    .orElseThrow(() -> new IllegalStateException("Unidade não encontrada após exclusão de review!"));

            atualizarMediaAvaliacaoUnidade(unidade);
        }
    }

    @Transactional
    public Review atualizarReview(Long id, Review novaAvaliacao){
        Review original = reviewRepository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("Avaliação com ID " + id + " não encontrada!"));

        original.update(novaAvaliacao);
        Review reviewAtualizada = reviewRepository.save(original);


        Unit unidade = unitRepository.findById(reviewAtualizada.getUnit().getId())
                .orElseThrow(() -> new IllegalStateException("Unidade não encontrada após atualização de review!"));

        atualizarMediaAvaliacaoUnidade(unidade);

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

        if (reviews.isEmpty()){
            throw new ReviewListForReviewerNotFoundException("Não há nenhuma avaliação registrada nesta conta!");
        }

        return reviews.stream().map(ReviewListDTO::fromEntity).toList();
    }
}
