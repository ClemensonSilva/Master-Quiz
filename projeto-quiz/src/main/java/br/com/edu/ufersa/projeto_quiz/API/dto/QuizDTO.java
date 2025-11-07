package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Classe usada para adição de quizes à disciplinas
 */
public class QuizDTO {
    @NotBlank
    private String titulo;
}
