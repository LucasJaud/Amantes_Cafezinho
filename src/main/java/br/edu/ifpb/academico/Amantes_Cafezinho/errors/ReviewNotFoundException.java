package br.edu.ifpb.academico.Amantes_Cafezinho.errors;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String message) {
        super(message);
    }
}
