package br.com.edu.ufersa.projeto_quiz.API.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * Classe que será retornada ao se criar uma noval relação aluno-disciplina: matricula.
 */

// TODO aplica hateoas
// TODO adicionar a data da matricula ao relacionamento
public class MatriculaResponseDTO {
    private DisciplinaDTO disciplinaDTO;
    private  ReturnAlunoDTO returnAlunoDTO;
}
