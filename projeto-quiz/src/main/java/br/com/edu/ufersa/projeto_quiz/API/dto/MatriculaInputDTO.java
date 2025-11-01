package br.com.edu.ufersa.projeto_quiz.API.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Classe responsável pela associação aluno-disciplina.
 * É usada para o Aluno se matricular em uma determinada disciplina
 */
public class MatriculaInputDTO {
    @NotNull(message = "O ID da disciplina é obrigatório.")
    private Long disciplinaId;

}
