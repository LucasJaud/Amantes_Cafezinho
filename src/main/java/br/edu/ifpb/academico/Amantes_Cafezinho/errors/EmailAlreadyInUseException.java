package br.edu.ifpb.academico.Amantes_Cafezinho.errors;

public class EmailAlreadyInUseException extends RuntimeException {
    public EmailAlreadyInUseException(String email) {
        super("O e-mail '" + email + "' já está em uso.");
    }
}