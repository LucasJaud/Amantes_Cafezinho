package br.edu.ifpb.academico.Amantes_Cafezinho.errors;

public class UserNotFound extends RuntimeException {
    public UserNotFound(Long id) {
        super("User with "+ id + " not found");
    }
}
