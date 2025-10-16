package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Professor;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
// TODO criar um DisciplinaDTOResponde com os atributos como Professor, list<quiz> e set<alunos>
public class DisciplinaDTO {
    @NotBlank(message = "A disciplina precisa de um nome definido!")
    @Size(max = 25, message = "O nome da disciplina deve ter até 25 caracteres!")
    private String nome;
    @NotEmpty(message = "A disciplina precisa de professor associado!")
    private Long professorId;

    public static DisciplinaDTO convert(Disciplina disciplina) {
        DisciplinaDTO dto = new DisciplinaDTO();
        dto.setNome(disciplina.getNome());
        dto.setProfessorId(disciplina.getProfessor().getId());
        return dto;
    }
}
