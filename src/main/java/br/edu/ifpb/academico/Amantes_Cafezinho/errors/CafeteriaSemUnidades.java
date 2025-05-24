package br.edu.ifpb.academico.Amantes_Cafezinho.errors;

public class CafeteriaSemUnidades extends RuntimeException{

    public CafeteriaSemUnidades(String message) {
        super(message);
    }

    public CafeteriaSemUnidades() {
        super("A cafeteria não possui unidades cadastradas.");
    }

    
}
