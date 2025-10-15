package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Alternativa;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;

import java.util.List;

public class QuestaoDTO {
    private Long id;
    private String descricao;
    private Alternativa alternativaCorreta;
    private List<Alternativa> alternativas;
    private Quiz quiz;

    public static QuestaoDTO convert(Questao questao){
        QuestaoDTO dto = new QuestaoDTO();
        dto.setId(questao.getId());
        dto.setDescricao(questao.getDescricao());
        dto.setAlternativaCorreta(questao.getAlternativaCorreta());
        dto.setAlternativas(questao.getAlternativas());
        dto.setQuiz(questao.getQuiz());
        return dto;
    }
}
