package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
// Esse dto é o que o usuario verá ao requisitar por uma Disciplina
public class DisciplinaDTOResponse {
    private String nome;
    private List<QuizDTO> quizes;
    private ReturnProfessorDTO professor;
    private Set<AlunoDTO> alunos;

    public static DisciplinaDTOResponse convert(Disciplina  disciplina) {
        DisciplinaDTOResponse dto = new DisciplinaDTOResponse();
        dto.setNome(disciplina.getNome());
        dto.setQuizes(disciplina.getQuizes().stream().map(QuizDTO::convert).collect(Collectors.toList()));
        dto.setProfessor(ReturnProfessorDTO.convert(disciplina.getProfessor()));
        dto.setAlunos(disciplina.getAluno().stream().map(AlunoDTO::convert).collect(Collectors.toSet()));
        return dto;
    }
}


