package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor

public class AlunoDTO {
    private Long id;
    private String nome;
    private String email;

    private Set<Disciplina> disciplinas ;

    public static AlunoDTO convert(Aluno aluno){
        AlunoDTO alunoDTO = new AlunoDTO();
        alunoDTO.setId(aluno.getId());
        alunoDTO.setNome(aluno.getNome());
        alunoDTO.setEmail(aluno.getEmail());
        alunoDTO.setDisciplinas(new HashSet<>());
        return alunoDTO;
    }

}
