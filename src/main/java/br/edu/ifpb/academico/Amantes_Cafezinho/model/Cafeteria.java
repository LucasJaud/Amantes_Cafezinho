package br.edu.ifpb.academico.Amantes_Cafezinho.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;


@Entity(name = "Cafeteria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cafeteria extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String CNPJ;

    private String razaoSocial;

    private String nomeFantasia;

    @OneToMany(mappedBy = "cafeteria", cascade = CascadeType.ALL)
    private List<Unidade> unidades;
}
