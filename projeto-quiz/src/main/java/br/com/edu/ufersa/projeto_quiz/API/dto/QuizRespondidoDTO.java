package br.com.edu.ufersa.projeto_quiz.API.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class QuizRespondidoDTO {
    private Long id;
    private QuizDTOResponse quiz;
    private Double pontuacaoFinal;
    private LocalDateTime dataTentativa;
    private Set<RespostaInputDTO> respostasDTO;
}
