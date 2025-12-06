package br.com.edu.ufersa.projeto_quiz.API.dto;

import jakarta.validation.Valid;
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
 * Classe usada ao associar uma lista de questões a um quiz específico. O ID do quiz é obitdo na url.
 * Esse DTO é usado no frontEnd para o user Professor adicionar uma questão a um quiz
 */
public class QuestaoDTO extends RepresentationModel<QuestaoDTO> {
    @NotBlank
    @Size(min = 5, max = 100, message = "Descrição fora do escopo de 5 a 100 caracteres")
    private String descricao;
    private List<@Valid     @NotNull AlternativaDTO> alternativas;


}
