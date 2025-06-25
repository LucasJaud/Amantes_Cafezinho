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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private UnitRepository unitRepository;

    @InjectMocks
    private ReviewService reviewService;

    private Review testReview1;
    private Review testReview2;
    private Reviewer testReviewer;
    private Unit testUnit;
    private Status testStatus;

    @BeforeEach
    void setUp() {
        testReviewer = new Reviewer();
        testReviewer.setId(1L);
        testReviewer.setFullName("Avaliador Teste");

        testUnit = new Unit();
        testUnit.setId(1L);
        testUnit.setName("Unidade Teste");
        testUnit.setAverage(0.0);

        testReview1 = new Review();
        testReview1.setId(1L);
        testReview1.setRating(5);
        testReview1.setContent("Excelente café e ambiente!");
        testReview1.setReviewer(testReviewer);
        testReview1.setUnit(testUnit);
        testReview1.setDatetime(LocalDate.of(2024, 1, 1));

        testReview2 = new Review();
        testReview2.setId(2L);
        testReview2.setRating(4);
        testReview2.setContent("Bom café, mas ambiente barulhento.");
        testReview2.setReviewer(testReviewer);
        testReview2.setUnit(testUnit);
        testReview2.setDatetime(LocalDate.of(2024, 2, 1));

        testStatus = new Status();
        testStatus.setId(1L);
        testStatus.setType("PENDING");
    }

    @Test
    @DisplayName("Deve criar uma nova review e atualizar a média da unidade")
    void criarReview_ShouldSaveReviewAndUpdateUnitAverage() {
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview1);

        // Mock para buscar a unidade, simulando que ela já existe para o recalculo
        // É importante que o mock para findById da unidade retorne um Optional da *mesma instância*
        // que o testReview1 está associado, ou uma cópia que será manipulada.
        // Se testUnit é a mesma instância, o mock abaixo garante que ela é "encontrável"
        when(unitRepository.findById(testUnit.getId())).thenReturn(Optional.of(testUnit));

        when(unitRepository.findAverageRatingByUnitId(testUnit.getId())).thenReturn(Optional.of(5.0));
        when(unitRepository.save(any(Unit.class))).thenReturn(testUnit); // Retorna a mesma instância de testUnit

        Review createdReview = reviewService.criarReview(testReview1);

        assertNotNull(createdReview);
        assertEquals(testReview1.getId(), createdReview.getId());
        assertEquals(testReview1.getContent(), createdReview.getContent());

        verify(reviewRepository, times(1)).save(testReview1);
        verify(unitRepository, times(1)).findById(testUnit.getId()); // Verifica a busca da unidade
        verify(unitRepository, times(1)).findAverageRatingByUnitId(testUnit.getId());
        // Verifica se a unidade foi salva com a nova média
        verify(unitRepository, times(1)).save(argThat(unit -> unit.getAverage().equals(5.0)));
    }

    @Test
    @DisplayName("Deve excluir uma review existente e atualizar a média da unidade")
    void excluirReview_ShouldDeleteReviewAndUpdateUnitAverage() {
        Long reviewIdToDelete = 1L;

        when(reviewRepository.findById(reviewIdToDelete)).thenReturn(Optional.of(testReview1));
        doNothing().when(reviewRepository).deleteById(reviewIdToDelete);

        // Mock para buscar a unidade antes de recalcular
        when(unitRepository.findById(testUnit.getId())).thenReturn(Optional.of(testUnit));

        when(unitRepository.findAverageRatingByUnitId(testUnit.getId())).thenReturn(Optional.of(4.0));
        when(unitRepository.save(any(Unit.class))).thenReturn(testUnit);

        reviewService.excluirReview(reviewIdToDelete);

        verify(reviewRepository, times(1)).findById(reviewIdToDelete);
        verify(reviewRepository, times(1)).deleteById(reviewIdToDelete);
        verify(unitRepository, times(1)).findById(testUnit.getId()); // Verifica a busca da unidade
        verify(unitRepository, times(1)).findAverageRatingByUnitId(testUnit.getId());
        verify(unitRepository, times(1)).save(argThat(unit -> unit.getAverage().equals(4.0)));
    }

    @Test
    @DisplayName("Não deve excluir review nem atualizar média se ID não existir")
    void excluirReview_ShouldNotDeleteOrUpdateIfIdDoesNotExist() {
        Long nonExistentId = 99L;
        when(reviewRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        reviewService.excluirReview(nonExistentId);

        verify(reviewRepository, times(1)).findById(nonExistentId);
        verify(reviewRepository, never()).deleteById(anyLong());
        verify(unitRepository, never()).findById(anyLong()); // Não deve buscar a unidade
        verify(unitRepository, never()).findAverageRatingByUnitId(anyLong());
        verify(unitRepository, never()).save(any(Unit.class));
    }


    @Test
    @DisplayName("Deve atualizar uma review existente e a média da unidade")
    void atualizarReview_ShouldUpdateReviewAndUnitAverage() {
        Long reviewIdToUpdate = 1L;
        Review novaAvaliacaoData = new Review();
        novaAvaliacaoData.setRating(3);
        novaAvaliacaoData.setContent("Conteúdo atualizado.");

        when(reviewRepository.findById(reviewIdToUpdate)).thenReturn(Optional.of(testReview1));
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview1);

        // Mock para buscar a unidade antes de recalcular
        when(unitRepository.findById(testUnit.getId())).thenReturn(Optional.of(testUnit));

        when(unitRepository.findAverageRatingByUnitId(testUnit.getId())).thenReturn(Optional.of(3.5));
        when(unitRepository.save(any(Unit.class))).thenReturn(testUnit);

        Review updatedReview = reviewService.atualizarReview(reviewIdToUpdate, novaAvaliacaoData);

        assertNotNull(updatedReview);
        assertEquals(testReview1.getId(), updatedReview.getId());
        assertEquals(novaAvaliacaoData.getRating(), updatedReview.getRating());
        assertEquals(novaAvaliacaoData.getContent(), updatedReview.getContent());

        verify(reviewRepository, times(1)).findById(reviewIdToUpdate);
        verify(reviewRepository, times(1)).save(testReview1);
        verify(unitRepository, times(1)).findById(testUnit.getId()); // Verifica a busca da unidade
        verify(unitRepository, times(1)).findAverageRatingByUnitId(testUnit.getId());
        verify(unitRepository, times(1)).save(argThat(unit -> unit.getAverage().equals(3.5)));
    }

    @Test
    @DisplayName("Deve lançar ReviewNotFoundException ao tentar atualizar review inexistente")
    void atualizarReview_ShouldThrowExceptionWhenNotFound() {
        Long nonExistentId = 99L;
        when(reviewRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () ->
                reviewService.atualizarReview(nonExistentId, new Review()));

        verify(reviewRepository, times(1)).findById(nonExistentId);
        verify(reviewRepository, never()).save(any(Review.class));
        verify(unitRepository, never()).findById(anyLong()); // Não deve buscar a unidade
        verify(unitRepository, never()).findAverageRatingByUnitId(anyLong());
        verify(unitRepository, never()).save(any(Unit.class));
    }


    @Test
    @DisplayName("Deve buscar uma review por ID e retornar a review")
    void buscarPorId_ShouldReturnReviewWhenFound() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview1));
        Review foundReview = reviewService.buscarPorId(1L);
        assertNotNull(foundReview);
        assertEquals(testReview1.getId(), foundReview.getId());
        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve buscar uma review por ID e retornar null quando não encontrada")
    void buscarPorId_ShouldReturnNullWhenNotFound() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());
        Review foundReview = reviewService.buscarPorId(99L);
        assertNull(foundReview);
        verify(reviewRepository, times(1)).findById(99L);
    }

    // Este teste foi ajustado para o cenário de Optional<Status>
    @Test
    @DisplayName("Deve buscar um status por tipo e retornar o Optional de Status")
    void buscarPorTipo_ShouldReturnOptionalOfStatusWhenFound() {
        when(statusRepository.findByType("PENDING")).thenReturn(Optional.of(testStatus));
        Optional<Status> foundStatus = reviewService.buscarPorTipo("PENDING");
        assertTrue(foundStatus.isPresent());
        assertEquals(testStatus.getType(), foundStatus.get().getType());
        verify(statusRepository, times(1)).findByType("PENDING");
    }

    // Este teste foi ajustado para o cenário de Optional<Status>
    @Test
    @DisplayName("Deve buscar um status por tipo e retornar Optional.empty quando não encontrado")
    void buscarPorTipo_ShouldReturnOptionalEmptyWhenNotFound() {
        when(statusRepository.findByType(anyString())).thenReturn(Optional.empty());
        Optional<Status> foundStatus = reviewService.buscarPorTipo("NON_EXISTENT");
        assertFalse(foundStatus.isPresent());
        verify(statusRepository, times(1)).findByType("NON_EXISTENT");
    }

    @Test
    @DisplayName("Deve listar reviews por unidade")
    void listarPorUnidade_ShouldReturnListOfReviews() {
        List<Review> reviews = Arrays.asList(testReview1, testReview2);
        when(reviewRepository.findByUnit(testUnit)).thenReturn(reviews);
        List<Review> foundReviews = reviewService.listarPorUnidade(testUnit);
        assertNotNull(foundReviews);
        assertFalse(foundReviews.isEmpty());
        assertEquals(2, foundReviews.size());
        verify(reviewRepository, times(1)).findByUnit(testUnit);
    }

    @Test
    @DisplayName("Deve listar reviews por avaliador e converter para DTOs")
    void listarPorAvaliador_ShouldReturnListOfReviewListDTOs() {
        List<Review> reviews = Arrays.asList(testReview1, testReview2);
        when(reviewRepository.findByReviewer(testReviewer)).thenReturn(reviews);
        List<ReviewListDTO> dtoList = reviewService.listarPorAvaliador(testReviewer);
        assertNotNull(dtoList);
        assertFalse(dtoList.isEmpty());
        assertEquals(2, dtoList.size());
        verify(reviewRepository, times(1)).findByReviewer(testReviewer);
    }

    @Test
    @DisplayName("Deve lançar ReviewListForReviewerNotFoundException quando listarPorAvaliador não encontra reviews")
    void listarPorAvaliador_ShouldThrowExceptionWhenNoReviewsFound() {
        when(reviewRepository.findByReviewer(testReviewer)).thenReturn(Collections.emptyList());
        assertThrows(ReviewListForReviewerNotFoundException.class, () ->
                reviewService.listarPorAvaliador(testReviewer));
        verify(reviewRepository, times(1)).findByReviewer(testReviewer);
    }
}