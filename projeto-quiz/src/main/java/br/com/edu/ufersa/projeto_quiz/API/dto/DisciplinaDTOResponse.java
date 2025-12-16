package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
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
// Esse dto é o que o usuario verá ao requisitar por uma Disciplina
public class DisciplinaDTOResponse extends RepresentationModel {
    private long id;
    private String nome;
    private ReturnProfessorDTO professor;
}


