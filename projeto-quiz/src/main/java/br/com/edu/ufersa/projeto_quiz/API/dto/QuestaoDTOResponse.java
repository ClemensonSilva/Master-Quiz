package br.com.edu.ufersa.projeto_quiz.API.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
/**
 * DTO que será no frontEnd para apresentar ao client os dados do formulario de resposta
 */
public class QuestaoDTOResponse extends RepresentationModel<QuestaoDTOResponse> {
    private long id;
    private String descricao;
    private List<AlternativaDTO> alternativas;

}