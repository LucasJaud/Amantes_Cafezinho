package br.edu.ifpb.academico.Amantes_Cafezinho.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @Column(nullable = false, length = 300)
    @Size(max = 300, message = "O conteúdo não pode ultrapassar 300 caracteres.")
    @NotBlank(message = "O conteúdo não pode estar em branco.")
    private String content;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    @NotNull(message = "A avaliação (nota) é obrigatória.")
    private     Integer rating;

    @ManyToOne
    @JoinColumn(name = "reviewer_id",nullable = false)
    private Reviewer reviewer;

    @ManyToOne
    @JoinColumn(name = "unit_id",nullable = false)
    private Unit unit;

    @ManyToOne
    @JoinColumn(name = "status_id",nullable = false)
    private Status status;

    @OneToMany(mappedBy = "review", fetch = FetchType.EAGER)
    private List<Comment> comments;
  
    @Column(nullable = true)
    private Boolean problemSolved = false;

    public void update (Review r){
        this.rating = r.getRating();
        this.content = r.getContent();
        this.datetime = LocalDate.now();
    }
}
