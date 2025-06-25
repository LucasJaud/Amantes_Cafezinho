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

        testReview1 = new Review();
        testReview1.setId(1L);
        testReview1.setRating(5);
        testReview1.setContent("Excelente café e ambiente!");
        testReview1.setReviewer(testReviewer);
        testReview1.setUnit(testUnit);
        testReview1.setDatetime(LocalDate.of(2024, 1, 1)); // Data fixa para o teste inicial

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
    @DisplayName("Deve criar uma nova avaliação com sucesso")
    void criarReview_ShouldSaveAndReturnReview() {
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview1);

        Review createdReview = reviewService.criarReview(testReview1);

        assertNotNull(createdReview);
        assertEquals(testReview1.getId(), createdReview.getId());
        assertEquals(testReview1.getContent(), createdReview.getContent());
        verify(reviewRepository, times(1)).save(testReview1);
    }

    @Test
    @DisplayName("Deve buscar uma avaliação por ID e retornar a avaliação")
    void buscarPorId_ShouldReturnReviewWhenFound() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview1));

        Review foundReview = reviewService.buscarPorId(1L);

        assertNotNull(foundReview);
        assertEquals(testReview1.getId(), foundReview.getId());
        assertEquals(testReview1.getContent(), foundReview.getContent());
        verify(reviewRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve buscar uma avaliação por ID e retornar null quando não encontrada")
    void buscarPorId_ShouldReturnNullWhenNotFound() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());

        Review foundReview = reviewService.buscarPorId(99L);

        assertNull(foundReview);
        verify(reviewRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve buscar um status por tipo e retornar o status")
    void buscarPorTipo_ShouldReturnStatusWhenFound() {
        when(statusRepository.findByType("PENDING")).thenReturn(testStatus);

        Status foundStatus = reviewService.buscarPorTipo("PENDING");

        assertNotNull(foundStatus);
        assertEquals(testStatus.getType(), foundStatus.getType());
        verify(statusRepository, times(1)).findByType("PENDING");
    }

    @Test
    @DisplayName("Deve buscar um status por tipo e retornar null quando não encontrado")
    void buscarPorTipo_ShouldReturnNullWhenNotFound() {
        when(statusRepository.findByType(anyString())).thenReturn(null);

        Status foundStatus = reviewService.buscarPorTipo("NON_EXISTENT");

        assertNull(foundStatus);
        verify(statusRepository, times(1)).findByType("NON_EXISTENT");
    }

    @Test
    @DisplayName("Deve listar avaliações por unidade")
    void listarPorUnidade_ShouldReturnListOfReviews() {
        List<Review> reviews = Arrays.asList(testReview1, testReview2);
        when(reviewRepository.findByUnit(testUnit)).thenReturn(reviews);

        List<Review> foundReviews = reviewService.listarPorUnidade(testUnit);

        assertNotNull(foundReviews);
        assertFalse(foundReviews.isEmpty());
        assertEquals(2, foundReviews.size());
        assertTrue(foundReviews.contains(testReview1));
        assertTrue(foundReviews.contains(testReview2));
        verify(reviewRepository, times(1)).findByUnit(testUnit);
    }

    @Test
    @DisplayName("Deve listar avaliações por avaliador e converter para DTOs")
    void listarPorAvaliador_ShouldReturnListOfReviewListDTOs() {
        List<Review> reviews = Arrays.asList(testReview1, testReview2);
        when(reviewRepository.findByReviewer(testReviewer)).thenReturn(reviews);

        List<ReviewListDTO> dtoList = reviewService.listarPorAvaliador(testReviewer);

        assertNotNull(dtoList);
        assertFalse(dtoList.isEmpty());
        assertEquals(2, dtoList.size());
        assertEquals(testReview1.getContent(), dtoList.get(0).content());
        assertEquals(testReview2.getRating(), dtoList.get(1).rating());
        verify(reviewRepository, times(1)).findByReviewer(testReviewer);
    }

    @Test
    @DisplayName("Deve lançar ReviewListForReviewerNotFoundException quando listarPorAvaliador não encontra avaliações")
    void listarPorAvaliador_ShouldThrowExceptionWhenNoReviewsFound() {
        when(reviewRepository.findByReviewer(testReviewer)).thenReturn(Collections.emptyList());

        assertThrows(ReviewListForReviewerNotFoundException.class, () ->
                reviewService.listarPorAvaliador(testReviewer));

        verify(reviewRepository, times(1)).findByReviewer(testReviewer);
    }

    @Test
    @DisplayName("Deve deletar uma avaliação existente com sucesso")
    void excluirReview_ShouldDeleteExistingReview() {
        when(reviewRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reviewRepository).deleteById(1L);

        reviewService.excluirReview(1L);

        verify(reviewRepository, times(1)).existsById(1L);
        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Não deve fazer nada ao tentar deletar uma avaliação inexistente")
    void excluirReview_ShouldDoNothingWhenReviewDoesNotExist() {
        when(reviewRepository.existsById(99L)).thenReturn(false);

        reviewService.excluirReview(99L);

        verify(reviewRepository, times(1)).existsById(99L);
        verify(reviewRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve atualizar uma avaliação existente com sucesso e definir a data atual")
    void atualizarReview_ShouldUpdateExistingReviewAndSetCurrentDate() {
        // Dados para a nova avaliação
        Review newReviewData = new Review();
        newReviewData.setRating(3);
        newReviewData.setContent("Café razoável. Melhorou um pouco.");
        // A data de newReviewData não importa, pois o método `update` no modelo sempre define `LocalDate.now()`

        // Dado que o repositório encontra a avaliação original (testReview1)
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(testReview1));
        // Quando `reviewRepository.save()` for chamado com `any(Review.class)`, retorne a instância de testReview1
        // Isso simula que o objeto `testReview1` foi atualizado e salvo.
        when(reviewRepository.save(any(Review.class))).thenReturn(testReview1);

        // Capturamos a data esperada ANTES de chamar o método, pois o método `update` usa `LocalDate.now()`
        LocalDate expectedDateAfterUpdate = LocalDate.now();

        // Quando o serviço for chamado para atualizar
        reviewService.atualizarReview(1L, newReviewData);

        // Então, o método findById deve ter sido chamado
        verify(reviewRepository, times(1)).findById(1L);
        // E o método save deve ter sido chamado com a avaliação que foi atualizada
        verify(reviewRepository, times(1)).save(testReview1);

        // Verifica se os campos de testReview1 foram atualizados (já que ele é o objeto real manipulado)
        assertEquals(3, testReview1.getRating());
        assertEquals("Café razoável. Melhorou um pouco.", testReview1.getContent());

        // Verifica se a data foi atualizada para a data corrente (do momento do teste)
        // Usamos `assertEquals` com `LocalDate.now()` no teste para verificar,
        // pois o método `update` do modelo Review usa `LocalDate.now()`.
        assertEquals(expectedDateAfterUpdate, testReview1.getDatetime());
    }


    @Test
    @DisplayName("Deve lançar ReviewNotFoundException ao tentar atualizar avaliação inexistente")
    void atualizarReview_ShouldThrowExceptionWhenNotFound() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ReviewNotFoundException.class, () ->
                reviewService.atualizarReview(99L, new Review()));

        verify(reviewRepository, times(1)).findById(99L);
        verify(reviewRepository, never()).save(any(Review.class));
    }
}