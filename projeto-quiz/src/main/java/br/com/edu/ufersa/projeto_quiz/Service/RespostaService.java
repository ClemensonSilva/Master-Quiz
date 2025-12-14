package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlunoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizRespondidoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.RespostaInputDTO;
import br.com.edu.ufersa.projeto_quiz.Model.entity.QuizRespondido;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Resposta;
import br.com.edu.ufersa.projeto_quiz.Model.repository.RespostaRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Service

public class RespostaService {

    @Autowired
    public RespostaRepository respostaRepository;
    @Autowired
    private ModelMapper modelMapper;

    public RespostaInputDTO create(RespostaInputDTO respostaAlunoDTO) {
        Resposta resposta = modelMapper.map(respostaAlunoDTO, Resposta.class);
        respostaRepository.save(resposta);
        return modelMapper.map(resposta, RespostaInputDTO.class);
    }
    public List<RespostaInputDTO> todasRespostaQuizRespondido(QuizRespondidoDTO quizRespondidoDTO) {

        return respostaRepository.findByQuizRespondido(modelMapper.map(quizRespondidoDTO, QuizRespondido.class))
                .stream()
                .map(x -> modelMapper.map(x, RespostaInputDTO.class))
                .collect(Collectors.toList());
    }
    public  List<RespostaInputDTO> respostasIncorretasQuizRespondido(QuizRespondidoDTO quizRespondidoDTO) {
        return respostaRepository.findByQuizRespondidoAndStatusRespostaTrue(modelMapper.map(quizRespondidoDTO, QuizRespondido.class))
                .stream()
                .map(x -> modelMapper.map(x, RespostaInputDTO.class))
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
