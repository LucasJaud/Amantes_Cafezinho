package br.edu.ifpb.academico.Amantes_Cafezinho.dtos;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Review;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReviewListDTO(
        @NotNull
        Long id,
        @NotNull
        Unit unit,
        @NotBlank
        String content,
        @NotNull
        Integer rating,
        @NotNull
        LocalDate datetime

) {
        public static ReviewListDTO fromEntity(Review review){
            return new ReviewListDTO(
                    review.getId(),
                    review.getUnit(),
                    review.getContent(),
                    review.getRating(),
                    review.getDatetime()
            );
        }
}
