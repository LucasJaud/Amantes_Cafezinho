package br.edu.ifpb.academico.Amantes_Cafezinho.models;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;


@Entity(name = "Cafeteria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cafeteria extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String CNPJ;

    private String socialReason;

    private String fantasyName;

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL)
    private List<Unit> units;
}
