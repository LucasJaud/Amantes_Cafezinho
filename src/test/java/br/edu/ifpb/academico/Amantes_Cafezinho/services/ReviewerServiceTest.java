package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Reviewer;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;
import br.edu.ifpb.academico.Amantes_Cafezinho.repositories.ReviewerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate; // Importa LocalDate
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewerServiceTest {

    @Mock
    private ReviewerRepository reviewerRepository;

    @InjectMocks
    private ReviewerService reviewerService;

    private User testUser;
    private Reviewer testReviewer;

    @BeforeEach
    void setUp() {
        // Configura um usuário de teste mais completo
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("teste_user");
        testUser.setEmail("teste@example.com");

        // Configura um avaliador de teste mais completo, usando os dados do modelo Reviewer
        testReviewer = new Reviewer();
        testReviewer.setId(10L);
        testReviewer.setUser(testUser); // Associa o usuário ao avaliador
        testReviewer.setCPF("123.456.789-00");
        testReviewer.setSex("M");
        testReviewer.setFullName("João Teste da Silva");
        testReviewer.setBirthDate(LocalDate.of(1990, 5, 15));
        // A lista de reviews não precisa ser mockada ou populada para este método específico
        // pois buscarPorUser() não a manipula diretamente.
    }

    @Test
    @DisplayName("Deve retornar um avaliador quando o usuário existe e está associado")
    void buscarPorUser_ShouldReturnReviewerWhenUserExists() {
        // Dado que o repositório retorna um Optional contendo o avaliador de teste
        when(reviewerRepository.findByUser(testUser)).thenReturn(Optional.of(testReviewer));

        // Quando o serviço for chamado para buscar por este usuário
        Reviewer foundReviewer = reviewerService.buscarPorUser(testUser);

        // Então, o avaliador retornado não deve ser nulo
        assertNotNull(foundReviewer);
        // E deve ser o avaliador de teste esperado, com todas as suas propriedades
        assertEquals(testReviewer.getId(), foundReviewer.getId());
        assertEquals(testReviewer.getFullName(), foundReviewer.getFullName());
        assertEquals(testReviewer.getCPF(), foundReviewer.getCPF());
        assertEquals(testReviewer.getUser().getId(), foundReviewer.getUser().getId()); // Verifica a associação do usuário
        // Verifica se o método findByUser do repositório foi chamado exatamente uma vez com o usuário correto
        verify(reviewerRepository, times(1)).findByUser(testUser);
    }

    @Test
    @DisplayName("Deve retornar null quando o usuário não tem um avaliador associado no repositório")
    void buscarPorUser_ShouldReturnNullWhenUserHasNoReviewer() {
        // Dado que o repositório retorna um Optional vazio (nenhum avaliador encontrado para este usuário)
        when(reviewerRepository.findByUser(testUser)).thenReturn(Optional.empty());

        // Quando o serviço for chamado para buscar por este usuário
        Reviewer foundReviewer = reviewerService.buscarPorUser(testUser);

        // Então, o avaliador retornado deve ser nulo
        assertNull(foundReviewer);
        // Verifica se o método findByUser do repositório foi chamado exatamente uma vez com o usuário correto
        verify(reviewerRepository, times(1)).findByUser(testUser);
    }

    @Test
    @DisplayName("Deve retornar null quando buscar por um usuário nulo")
    void buscarPorUser_ShouldReturnNullWhenUserIsNull() {
        // Mockito permite mockar chamadas com argumentos nulos.
        // Dado que o repositório retorna Optional.empty() quando findByUser é chamado com null
        when(reviewerRepository.findByUser(null)).thenReturn(Optional.empty());

        // Quando o serviço for chamado com um usuário nulo
        Reviewer foundReviewer = reviewerService.buscarPorUser(null);

        // Então, o avaliador retornado deve ser nulo
        assertNull(foundReviewer);
        // Verifica se o método findByUser do repositório foi chamado exatamente uma vez com null
        verify(reviewerRepository, times(1)).findByUser(null);
    }
}