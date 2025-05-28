package br.edu.ifpb.academico.Amantes_Cafezinho.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CafeteriaNotFoundException extends RuntimeException {
    public CafeteriaNotFoundException(String cnpj) {
        super("Cafeteria with CNPJ " + cnpj + " not found");
    }
}
