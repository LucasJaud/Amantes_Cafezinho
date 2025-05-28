package br.edu.ifpb.academico.Amantes_Cafezinho.errors;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.User;

public class UserIsNotCafeteria extends RuntimeException {
    public UserIsNotCafeteria(User user) {
        super("User"+ user.getUsername() + "is not a cafeteria");
    }
}
