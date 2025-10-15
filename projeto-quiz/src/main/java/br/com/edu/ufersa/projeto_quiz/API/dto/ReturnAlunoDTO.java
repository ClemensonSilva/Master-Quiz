package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;

public class ReturnAlunoDTO extends ReturnUsuarioDTO{
    public ReturnAlunoDTO() {}

    public ReturnAlunoDTO(Aluno aluno) {
        super(aluno);
    }
}
