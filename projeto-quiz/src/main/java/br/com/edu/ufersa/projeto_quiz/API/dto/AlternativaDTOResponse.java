package br.com.edu.ufersa.projeto_quiz.API.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/**
 * DTO usado na API para renderizar informações da questão no momento da resposta do aluno
 */
public class AlternativaDTOResponse {
    private Long id;
    @NotBlank
    private String descricao;

}
