package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank()
    @Size(max = 400)
    private String descricao;
}
