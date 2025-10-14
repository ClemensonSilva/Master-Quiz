package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlunoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizRespondidoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.RespostaDTO;
import br.com.edu.ufersa.projeto_quiz.Model.entity.QuizRespondido;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Resposta;
import br.com.edu.ufersa.projeto_quiz.Model.repository.RespostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service

public class RespostaService {

    @Autowired
    public RespostaRepository respostaRepository;

    public List<RespostaDTO> todasRespostaQuizRespondido(QuizRespondidoDTO quizRespondidoDTO) {

        return respostaRepository.findByQuizRespondido(QuizRespondido.convert(quizRespondidoDTO))
                .stream()
                .map(RespostaDTO::convert)
                .collect(Collectors.toList());
    }
    public  List<RespostaDTO> respostasIncorretasQuizRespondido(QuizRespondidoDTO quizRespondidoDTO) {
        return respostaRepository.findByQuizRespondidoAndStatusRespostaTrue(QuizRespondido.convert(quizRespondidoDTO))
                .stream()
                .map(RespostaDTO::convert)
                .collect(Collectors.toList());
    }
    // calculo do tempo medio de resposta do aluno
    public OptionalDouble tempoMedioRespostaAluno(AlunoDTO alunoDTO ) {
            return respostaRepository.findAll().stream()
                    .mapToLong(Resposta::getTempoResposta)
                    .average();
    }
    // TODO verificar se cabe um para todas as respostas incorretas no quiz
}
