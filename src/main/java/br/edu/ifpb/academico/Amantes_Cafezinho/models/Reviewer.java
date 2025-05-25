package br.edu.ifpb.academico.Amantes_Cafezinho.models;
import java.sql.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Reviewer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Reviewer extends User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String CPF;

    private String sex;

    private String fullName;

    private Date birthDate;

    @OneToMany(mappedBy = "reviewer", fetch = FetchType.EAGER)
    private List<Review> review;

}
