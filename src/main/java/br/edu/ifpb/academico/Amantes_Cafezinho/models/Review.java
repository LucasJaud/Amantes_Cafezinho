package br.edu.ifpb.academico.Amantes_Cafezinho.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate datetime;

    @ManyToOne
    @JoinColumn(name = "cafeteria_id",nullable = false)
    private Reviewer reviewer;

    @ManyToOne
    @JoinColumn(name = "unit_id",nullable = false)
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "status_id",nullable = false)
    private Status status;

    @OneToMany(mappedBy = "review", fetch = FetchType.EAGER)
    private List<Comment> comments;
}
