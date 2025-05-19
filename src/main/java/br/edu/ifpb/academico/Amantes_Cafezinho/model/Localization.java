package br.edu.ifpb.academico.Amantes_Cafezinho.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "Localization")
@NoArgsConstructor
@AllArgsConstructor
public class Localization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String CEP;
    private String cidade;
    private String estado;
    private String numero;
    private String rua;
    private String bairro;
    private String complemento;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

}
