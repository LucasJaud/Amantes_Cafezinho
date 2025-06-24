package br.edu.ifpb.academico.Amantes_Cafezinho.services;

import java.util.List;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ifpb.academico.Amantes_Cafezinho.errors.CafeteriaSemUnidades;

@Service
public class FachadaService {

    @Autowired
    private UnitService unitService;

    @Autowired
    private CafeteriaService cafeteriaService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewerService reviewerService;


    public Unit criarUnidade(Unit unidade) {

        unitService.temUnidadeComCnpj(unidade.getCnpj());
        unitService.cadastrarUnidade(unidade);

        return unidade;

    }

    public Cafeteria resgatarCafeteriaPorCNPJ(String cnpj) {

        return cafeteriaService.resgatarCafeteriaPorCNPJ(cnpj);

    }

    public Unit resgatarUnidadePorId(Long id) {

        return unitService.resgatarUnidadePorId(id);

    }

    public List<Unit> resgatarUnidadesPorCafeteria(Cafeteria cafeteria) throws CafeteriaSemUnidades{

        List<Unit> unidadesResgatadas = unitService.resgatarUnidadesPorCafeteria(cafeteria);

//        if (unidadesResgatadas.isEmpty()) {
//            throw new CafeteriaSemUnidades();
//        }

        return unidadesResgatadas;

    }

    public Review criarAvaliacao(Review review) {
        return reviewService.criarReview(review);
    }

    public Reviewer buscarReviewerPorUser(User user) {
        return reviewerService.buscarPorUser(user);
    }

    public Cafeteria buscarCafeteriaPorUser(User user) {
        return cafeteriaService.buscarPorUser(user);
    }

    public void excluirReview(Long unidade) {
        reviewService.excluirReview(unidade);
    }
}
