package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Professor;

public class ReturnProfessorDTO extends ReturnUsuarioDTO{
    public ReturnProfessorDTO() {}

    public ReturnProfessorDTO(Professor professor) {
        super(professor);
    }
}
