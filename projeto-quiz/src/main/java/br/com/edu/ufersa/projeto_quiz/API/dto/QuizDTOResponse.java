package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class QuizDTOResponse extends RepresentationModel<QuizDTOResponse> {
    private long id;
    private long disciplinaId;
    private String titulo;
    private List<QuestaoDTOResponse> questoes;
}
