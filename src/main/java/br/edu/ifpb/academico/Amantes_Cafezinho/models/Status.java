package br.edu.ifpb.academico.Amantes_Cafezinho.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Status {

    @Id
    private int id;

    private String type;

    @OneToMany(mappedBy = "Status")
    private List<Review> reviews;

}
