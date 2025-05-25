package br.edu.ifpb.academico.Amantes_Cafezinho.errors;

public class UsernameAlreadyInUseException extends RuntimeException {
    public UsernameAlreadyInUseException(String username) {
        super("O nome de usuário '" + username + "' já está em uso.");
    }
}