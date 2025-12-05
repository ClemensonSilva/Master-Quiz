package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class QuizDTOResponse extends RepresentationModel<QuizDTOResponse> {
    private long id;
    private String titulo;

//    public static  QuizDTOResponse convert(Quiz quiz) {
//        QuizDTOResponse response = new QuizDTOResponse();
//        response.setTitulo(quiz.getTitulo());
//        response.setDisciplina(DisciplinaDTO.convert(quiz.getDisciplina()));
//        response.setQuestoes(quiz.getQuestoes().stream().map(QuestaoDTOResponse::convert).collect(Collectors.toSet()));
//        return response;
//    }
}
