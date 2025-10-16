package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Alternativa;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AlternativaDTO {
        private Long id;
        private String descricao;

        public static AlternativaDTO  convert(Alternativa alternativa){
            AlternativaDTO alternativaDTO = new AlternativaDTO();
            alternativaDTO.setId(alternativa.getId());
            alternativaDTO.setDescricao(alternativa.getDescricao());
            return alternativaDTO;
        }
}
