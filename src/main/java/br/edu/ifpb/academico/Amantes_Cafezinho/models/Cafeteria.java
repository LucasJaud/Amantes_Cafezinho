package br.edu.ifpb.academico.Amantes_Cafezinho.models;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CNPJ;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cafeteria{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

//    @CNPJ
    @Column(unique = true)
    private String CNPJ;

    private String photoPath;

    private String socialReason;

    private String fantasyName;

    @OneToMany(mappedBy = "cafeteria", cascade = CascadeType.ALL)
    private List<Unit> units;
}
