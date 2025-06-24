package br.edu.ifpb.academico.Amantes_Cafezinho.dtos;

import br.edu.ifpb.academico.Amantes_Cafezinho.models.Review;
import br.edu.ifpb.academico.Amantes_Cafezinho.models.Unit;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ReviewListDTO(
        @NotBlank
        Long id,
        @NotBlank
        Unit unit,
        @NotBlank
        String content,
        @NotBlank
        Integer rating,
        @NotBlank
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
