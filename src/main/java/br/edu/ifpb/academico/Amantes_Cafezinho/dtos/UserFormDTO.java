package br.edu.ifpb.academico.Amantes_Cafezinho.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record UserFormDTO(
        // Comum
        @Email(message = "Email inválido!")
        String email,
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "A senha deve ter no mínimo 8 caracteres, incluindo letras e números.")
        String password,
        String username,

        // Seletor do tipo de usuário a ser cadastrado ("REVIEWER" ou "CAFETERIA")
        String userType,

        // Reviewer
        @CPF(message = "CPF inválido!")
        String cpf,
        String fullName,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate birthDate,
        String sex,

        // Cafeteria
        @CNPJ(message = "CNPJ inválido!")
        String cnpj,
        String socialReason,
        String fantasyName
) {
        // Construtor vazio inicializando com valores padrão
        public UserFormDTO() {
                this("", "","", "", "", "", null, "", "", "", "");
        }
}
