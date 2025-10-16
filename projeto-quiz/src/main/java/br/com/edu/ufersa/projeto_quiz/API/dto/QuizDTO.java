package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class QuizDTO {

    private String titulo;
   // private Set<Questao> questoes;
    private Long  disciplinaId;

    public static QuizDTO convert(Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setTitulo(quiz.getTitulo());
        quizDTO.setDisciplinaId(quiz.getDisciplina().getId());
        //quizDTO.setQuestoes(quiz.getQuestoes());
        return quizDTO;
    }
}
