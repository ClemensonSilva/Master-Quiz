package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter

public class QuizDTO {
    private Long id;
    private String titulo;
    private Set<Questao> questoes;
    private Disciplina disciplina;

    public static QuizDTO convert(Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(quiz.getId());
        quizDTO.setTitulo(quiz.getTitulo());
        quizDTO.setDisciplina(quiz.getDisciplina());
        quizDTO.setQuestoes(quiz.getQuestoes());
        return quizDTO;
    }
}
