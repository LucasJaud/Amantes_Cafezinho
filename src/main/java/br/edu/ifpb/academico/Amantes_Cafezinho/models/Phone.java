package br.edu.ifpb.academico.Amantes_Cafezinho.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String DDD;
    private String number;
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    
}
