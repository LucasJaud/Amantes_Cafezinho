package br.edu.ifpb.academico.Amantes_Cafezinho.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comment {

    @Id
    private int id;

    @Column(nullable = false, length = 300)
    private String content;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
