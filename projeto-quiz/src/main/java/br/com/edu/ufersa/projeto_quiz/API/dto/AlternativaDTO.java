package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Alternativa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@ToString
/**
 * DTO usado pela API para criação de questões pelo professor.
 */
public class AlternativaDTO  {
        private  Long id;
        @NotBlank
        private String descricao;
        @NotNull
        private Boolean correta;

    public boolean isCorreta(){
        return this.correta;
    }
}
