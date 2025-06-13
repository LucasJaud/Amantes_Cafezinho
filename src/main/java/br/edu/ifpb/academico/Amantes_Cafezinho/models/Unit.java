package br.edu.ifpb.academico.Amantes_Cafezinho.models;

import jakarta.persistence.*;
//import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cafeteria_id",nullable = false)
    private Cafeteria cafeteria;

    @NotBlank(message = "O nome é obrigatório")
    private String name;

    // Diferente da Matriz
    @NotBlank(message = "O CNPJ é obrigatório")
    @Pattern(
    regexp = "\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}",
    message = "CNPJ inválido. Use o formato 00.000.000/0000-00")
    private String cnpj;

    private boolean isActive = true;

    @NotBlank(message = "A categoria de preços é obrigatória")
    private String priceCategory;

    @OneToMany(mappedBy = "unit",fetch = FetchType.EAGER)
    private List<Review> reviews;

    @Override
    public String toString() {
        return name != null ? name : "No name";
    }
    
}
