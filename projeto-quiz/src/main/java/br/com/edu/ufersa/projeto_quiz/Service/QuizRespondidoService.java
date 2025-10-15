package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlunoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizRespondidoDTO;
import br.com.edu.ufersa.projeto_quiz.Model.entity.*;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuestaoRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuizRespondidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

// TODO aplicar excessoes de regra de negocio
@Service
public class QuizRespondidoService {
    @Autowired
    private QuizRespondidoRepository quizRespondidoRepository;
    @Autowired
    private QuestaoRepository questaoRepository;

    public QuizRespondidoDTO createQuiz(QuizRespondidoDTO dto) {

        // TODO lancar excessoes para Alunos, Questoes, Alternativas nao cadastradas no DB

        QuizRespondido quizRespondido  = QuizRespondido.convert(dto);
        Alternativa altCorreta;
        Double taxaAcerto = 0D;

        // Ids das questoes
        Set<Long> questaoId = quizRespondido.getRespostas().stream()
                .map(resposta -> resposta
                                .getQuestao()
                                .getId())
                                .collect(Collectors.toSet());

        // criacao do map para rapidez
        Map<Long , Questao> questoesMap = questaoRepository.findAllById(questaoId)
                .stream()
                .collect(Collectors.toMap(Questao::getId, questao -> questao));

        quizRespondido.setDataTentativa(LocalDate.now());
        Questao questaoDB;

        // passar pelas respostas dos alunos e coletar o idQuestao
        for(Resposta resposta  : quizRespondido.getRespostas() ){
            questaoDB = questoesMap.get(resposta.getQuestao().getId());
            altCorreta = questaoDB.getAlternativaCorreta();

            // verALifica se a alternativaCorreta do DB é igual à alternatia correta do aluno
            if( altCorreta!= null && altCorreta.getId().equals(resposta.getAlternativaEscolhida().getId())){
                resposta.setStatusResposta(true);
                taxaAcerto+= 1;
            }else {
                resposta.setStatusResposta(false);
            }
        }

        // taxa de respostas corretas
        quizRespondido.setPontuacaoFinal((100*taxaAcerto)/quizRespondido.getRespostas().size());
        quizRespondidoRepository.save(quizRespondido);

        return QuizRespondidoDTO.convert(quizRespondido);
    }

    //  ver com mais detalhes o PageRequest
    public List<QuizRespondidoDTO> findAll() {
         return quizRespondidoRepository.findAll()
                 .stream()
                 .map(QuizRespondidoDTO::convert).
                 collect(Collectors.toList());
    }
    // TODO preciso que o QuizDTO seja implementado para prosseguir
//    public List<QuizRespondidoDTO> findAllByQuiz(Quiz quiz) {
//        return quizRespondidoRepository.findQuizRespondidoByQuiz(quiz)
//    }

    public List<QuizRespondidoDTO> quizesAlunoPeriodo(Aluno aluno, LocalDate data_inicio, LocalDate data_fim) {
       return   quizRespondidoRepository
                .findByAlunoAndDataTentativaBetween(aluno, data_inicio, data_fim)
                 .stream()
                 .map(QuizRespondidoDTO::convert)
                 .collect(Collectors.toList());
    }

    public List<QuizRespondidoDTO> quizesRespondidosPorAluno(AlunoDTO alunoDto) {
        return quizRespondidoRepository
                .findByAluno(Aluno.convert(alunoDto))
                .stream()
                .map(QuizRespondidoDTO::convert)
                .collect(Collectors.toList());
    }
    // TODO criar meio de calcular a pontuacao de um aluno baseada em suas respostas dos quizes
}
