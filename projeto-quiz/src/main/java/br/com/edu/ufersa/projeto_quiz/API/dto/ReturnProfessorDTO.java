package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Professor;

public class ReturnProfessorDTO extends ReturnUsuarioDTO{
    public ReturnProfessorDTO() {}

    public ReturnProfessorDTO(Professor professor) {
        super(professor);
    }

    public static ReturnProfessorDTO convert(Professor professor) {
        ReturnProfessorDTO dto = new ReturnProfessorDTO();
        dto.setId(professor.getId());
        dto.setNome(professor.getNome());
        return dto;
    }
}
