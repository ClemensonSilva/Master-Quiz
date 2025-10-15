package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

// Fiz essa classe pq a criacao da classe INputAlunoDTO quebrou a minha parte do projeto. Depois tento resolver
@Setter
@Getter
@ToString
@NoArgsConstructor
public class AlunoDTO {
    public static  AlunoDTO toDTO(Aluno aluno) {
        AlunoDTO alunoDTO = new AlunoDTO();
        BeanUtils.copyProperties(aluno, alunoDTO);
        return alunoDTO;
    }
}
