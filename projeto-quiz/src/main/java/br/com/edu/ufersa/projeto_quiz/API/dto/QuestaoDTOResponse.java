package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Alternativa;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuestaoDTOResponse {
    private String descricao;
    private AlternativaDTO alternativaCorreta;
    private List<AlternativaDTO> alternativas;
    private QuizDTO quiz;
//
//    public static QuestaoDTOResponse convert(Questao questao){
//        QuestaoDTOResponse dto = new QuestaoDTOResponse();
//        dto.setDescricao(questao.getDescricao());
//        dto.setAlternativaCorreta(AlternativaDTO.convert(questao.getAlternativaCorreta()));
//        dto.setAlternativas(questao.getAlternativas().stream().map(q -> AlternativaDTO.convert(q)).collect(Collectors.toList()));
//        dto.setQuiz(QuizDTO.convert(questao.getQuiz()));
//        return dto;
//    }
}