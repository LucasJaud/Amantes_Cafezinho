package br.edu.ifpb.academico.Amantes_Cafezinho.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 300)
    private String content;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "review_id",nullable = false)
    private Review review;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}
