package br.edu.ifpb.academico.Amantes_Cafezinho.errors;

public class CNPJExistente extends RuntimeException{

     public CNPJExistente(String message) {
        super(message);
    }

    public CNPJExistente() {
        super("Esse CNPJ já está cadastrado.");
    }

    
}
