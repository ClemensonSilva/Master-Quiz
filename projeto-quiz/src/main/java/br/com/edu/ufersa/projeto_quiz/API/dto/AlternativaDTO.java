package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Alternativa;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@ToString
public class AlternativaDTO  {
        private Long id;
        @NotBlank
        private String descricao;
        @NotBlank
        private long questaoId;

        public static AlternativaDTO  convert(Alternativa alternativa){
            AlternativaDTO alternativaDTO = new AlternativaDTO();
            alternativaDTO.setId(alternativa.getId());
            alternativaDTO.setDescricao(alternativa.getDescricao());
            return alternativaDTO;
        }
}
