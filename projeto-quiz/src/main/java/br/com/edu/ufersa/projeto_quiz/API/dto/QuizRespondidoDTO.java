package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import br.com.edu.ufersa.projeto_quiz.Model.entity.QuizRespondido;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Resposta;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor

public class QuizRespondidoDTO {
    private Long id;
    private Aluno aluno;
    private Quiz quiz;
    private Set<RespostaDTO> respostasDTO;

    public static QuizRespondidoDTO convert(QuizRespondido qr){
        QuizRespondidoDTO dto = new QuizRespondidoDTO();
        dto.setId(qr.getId());
        dto.setAluno(qr.getAluno());
        dto.setQuiz(qr.getQuiz());
        dto.setRespostasDTO(qr.getRespostas()
                .stream()
                .map(resposta -> RespostaDTO.convert(resposta)).collect(Collectors.toSet()));
        return dto;
    }

}
