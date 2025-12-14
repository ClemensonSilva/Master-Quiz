package br.com.edu.ufersa.projeto_quiz.API.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RespostaInputDTO {
    private Long questaoId;
    private Long alternativaId;
    private long tempoResposta;
}
