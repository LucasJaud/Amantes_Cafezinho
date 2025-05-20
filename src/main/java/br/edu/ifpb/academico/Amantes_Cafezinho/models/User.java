package br.edu.ifpb.academico.Amantes_Cafezinho.models;


import java.sql.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity(name = "User")
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private Date createdAt;

    private String username;

    @OneToMany(mappedBy = "User", fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private List<Phone> phones;

    @OneToMany(mappedBy = "User", fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private List<Localization> localization;

    @OneToMany(mappedBy = "User", fetch = FetchType.EAGER)
    private List<Comment> comments;

}
