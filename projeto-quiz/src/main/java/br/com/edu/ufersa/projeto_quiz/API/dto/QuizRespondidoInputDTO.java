package br.com.edu.ufersa.projeto_quiz.API.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private long alunoId;
    @NotEmpty
    private List<@NotNull RespostaInputDTO> respostas;
}