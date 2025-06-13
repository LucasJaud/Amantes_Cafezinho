package br.edu.ifpb.academico.Amantes_Cafezinho.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Localization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zipCode;
    private String city;
    private String state;
    private String number;
    private String street;
    private String neighborhood;
    private String complement;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
