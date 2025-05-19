package br.edu.ifpb.academico.Amantes_Cafezinho.model;
import java.sql.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Avaliador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Avaliador extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String CPF;

    private String sexo;

    private String nomeCompleto;

    private Date dataNascimento;

}
