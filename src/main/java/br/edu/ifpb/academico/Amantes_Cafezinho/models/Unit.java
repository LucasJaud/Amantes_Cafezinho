package br.edu.ifpb.academico.Amantes_Cafezinho.models;

import jakarta.persistence.*;
//import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cafeteria_id",nullable = false)
    private Cafeteria cafeteria;

    private String name;

    // Diferente da Matriz
    private String CNPJ;

    private boolean isActive;

    private String priceCategory;

    @OneToMany(mappedBy = "unit",fetch = FetchType.EAGER)
    private List<Review> reviews;

    
}
