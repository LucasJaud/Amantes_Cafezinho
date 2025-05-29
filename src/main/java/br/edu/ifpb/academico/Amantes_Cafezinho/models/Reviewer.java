package br.edu.ifpb.academico.Amantes_Cafezinho.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Reviewer{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "user_id", nullable = false, unique = true)
        private User user;

        @CPF
        private String CPF;

        private String sex;

        private String fullName;

        private LocalDate birthDate;

        @OneToMany(mappedBy = "reviewer", fetch = FetchType.EAGER)
        private List<Review> review;

        @Override
        public String toString() {
            return fullName != null ? fullName : "No name";
        }
    }
