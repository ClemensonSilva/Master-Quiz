package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Alternativa;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
/**
 * Classe usada ao associar uma lista de questões a um quiz específico. O id do quiz é obitdo na url.
 * Esse DTO é usado no frontEnd para o user Professor adicionar uma questão a um quiz
 */
public class QuestaoDTO extends RepresentationModel<QuestaoDTO> {
    @NotBlank
    @Size(min = 5, max = 25, message = "Descrição fora do escopo de 5 a 25 caracteres")
    private String descricao;
    @NotEmpty
    private AlternativaDTO alternativaCorreta;
    private List<@NotEmpty AlternativaDTO> alternativas;


}
