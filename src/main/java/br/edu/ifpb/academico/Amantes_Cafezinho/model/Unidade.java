package br.edu.ifpb.academico.Amantes_Cafezinho.model;

import jakarta.persistence.Entity;
//import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "Unidade")
@NoArgsConstructor
@AllArgsConstructor
public class Unidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(nullable = false)
    private Cafeteria cafeteria;

    private String nome;

    // Diferente da Matriz
    private String CNPJ;

    private boolean ativo;

    private String categoriaDePreco;

    
}
