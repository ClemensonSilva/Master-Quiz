package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class QuizDTOResponse {

    private String titulo;
    private Set<QuestaoDTOResponse> questoes;
    private DisciplinaDTO  disciplina;

//    public static  QuizDTOResponse convert(Quiz quiz) {
//        QuizDTOResponse response = new QuizDTOResponse();
//        response.setTitulo(quiz.getTitulo());
//        response.setDisciplina(DisciplinaDTO.convert(quiz.getDisciplina()));
//        response.setQuestoes(quiz.getQuestoes().stream().map(QuestaoDTOResponse::convert).collect(Collectors.toSet()));
//        return response;
//    }
}
