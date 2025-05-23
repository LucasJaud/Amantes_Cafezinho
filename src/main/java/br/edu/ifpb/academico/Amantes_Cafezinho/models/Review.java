package br.edu.ifpb.academico.Amantes_Cafezinho.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
public class Review {
    @Id
    private Long id;

    private Date datetime;

    @ManyToOne(optional = false)
    private Reviewer reviewer;

    @ManyToOne(optional = false)
    private Unit unit;

    @ManyToOne(optional = false)
    private Status status;

    @OneToMany
    private List<Comment> comments;
}
