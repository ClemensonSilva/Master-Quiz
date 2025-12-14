package br.com.edu.ufersa.projeto_quiz.API.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuizRespondidoInputDTO {
    private long alunoId;
    private List<RespostaInputDTO> respostas;
}