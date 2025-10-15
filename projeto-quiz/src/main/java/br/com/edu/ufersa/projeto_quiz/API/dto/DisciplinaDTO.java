package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Professor;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DisciplinaDTO {
    private Long id;
    private String nome;
    private List<Quiz> quizes;
    private Professor professor;
    private Set<Aluno> alunos;

    public static DisciplinaDTO convert(Disciplina disciplina){
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setId(disciplina.getId());
        dto.setNome(disciplina.getNome());
        dto.setQuizes(disciplina.getQuizes());
        dto.setProfessor(disciplina.getProfessor());
        dto.setAlunos(disciplina.getAluno());
        return dto;
    }
}
