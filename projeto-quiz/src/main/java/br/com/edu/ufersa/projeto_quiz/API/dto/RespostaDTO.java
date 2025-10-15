package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Alternativa;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.entity.QuizRespondido;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Resposta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RespostaDTO {

    private Long id;
    private Questao questao;
    private Alternativa alternativaEscolhida;
    private Long tempoResposta;

    public  static RespostaDTO convert(Resposta resposta){
        RespostaDTO respostaDTO = new RespostaDTO();
        respostaDTO.setId(resposta.getId());
        respostaDTO.setQuestao(resposta.getQuestao());
        respostaDTO.setAlternativaEscolhida(resposta.getAlternativaEscolhida());
        respostaDTO.setTempoResposta(resposta.getTempoResposta());
        return respostaDTO;
    }
}
