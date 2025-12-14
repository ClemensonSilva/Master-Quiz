package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlunoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizRespondidoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizRespondidoInputDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.RespostaInputDTO;
import br.com.edu.ufersa.projeto_quiz.Model.entity.*;
import br.com.edu.ufersa.projeto_quiz.Model.repository.*;
import br.com.edu.ufersa.projeto_quiz.exception.BusinessLogicException;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// TODO aplicar excessoes de regra de negocio
@Service
public class QuizRespondidoService {

    private final QuizRespondidoRepository quizRespondidoRepository;
    private final QuestaoRepository questaoRepository;
    private final AlunoRepository alunoRepository;
    private final ModelMapper modelMapper;
    private final QuizRepository quizRepository;
    private final AlternativaRepository alternativaRepository;


    @Autowired
    public QuizRespondidoService(QuizRespondidoRepository quizRespondidoRepository, QuestaoRepository questaoRepository, AlunoRepository alunoRepository, ModelMapper modelMapper, QuizRepository quizRepository, AlternativaRepository alternativaRepository) {
        this.quizRespondidoRepository = quizRespondidoRepository;
        this.questaoRepository = questaoRepository;
        this.alunoRepository = alunoRepository;
        this.modelMapper = modelMapper;
        this.quizRepository = quizRepository;
        this.alternativaRepository = alternativaRepository;
    }

    // TODO  ver com mais detalhes o PageRequest
    public List<QuizRespondidoDTO> findAll() {
         return quizRespondidoRepository.findAll()
                 .stream()
                 .map(x -> modelMapper.map(x, QuizRespondidoDTO.class)).
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
                 .map(x -> modelMapper.map(x, QuizRespondidoDTO.class) )
                 .collect(Collectors.toList());
    }

    public List<QuizRespondidoDTO> quizesRespondidosPorAluno(AlunoDTO alunoDto) {
        return quizRespondidoRepository
                .findByAluno(Aluno.convert(alunoDto))
                .stream()
                .map(x -> modelMapper.map(x, QuizRespondidoDTO.class))
                .collect(Collectors.toList());
    }

    public QuizRespondidoDTO responderQuiz(QuizRespondidoInputDTO dto, long quizId) throws BusinessLogicException, ResourceNotFound {

        Aluno aluno = alunoRepository.findAlunoById(dto.getAlunoId());

        if (aluno == null) {
            throw new BusinessLogicException("Aluno precisa estar logado para responder");
        }
        Quiz quiz = quizRepository.findQuizById(quizId);
        if (quiz == null) {
            throw new ResourceNotFound("Quiz não encontrado");
        }
        QuizRespondido ultimaTentaiva = quizRespondidoRepository.findQuizRespondidoByAlunoAndQuiz(aluno, quiz);
        QuizRespondido quizRespondido;

        if(ultimaTentaiva != null){
            quizRespondido = ultimaTentaiva;
            // limpa as respostas anteriores
            if(quizRespondido.getRespostas() != null){
                quizRespondido.getRespostas().clear();
            }else {
                quizRespondido.setRespostas(new HashSet<>());
            }
        }else {
            quizRespondido = new QuizRespondido();
            quizRespondido.setAluno(aluno);
            quizRespondido.setQuiz(quiz);
            quizRespondido.setRespostas(new HashSet<>());
        }

        int acertos = 0;
        double pontuacaoFinal;
        final int tamanhoQuiz = quiz.getQuestoes().size();
        Set<Resposta> respostasEntidade = new HashSet<>();

        for(RespostaInputDTO respostaAluno : dto.getRespostas()){
            Questao questao = questaoRepository.findQuestaoById(respostaAluno.getQuestaoId());
            if (questao == null) {
                throw  new BusinessLogicException("Não há questao para essa resposta.");
            }

            if(!questao.getQuiz().contains(quiz)){
                throw  new BusinessLogicException("A questão não esta associada ao quiz.");
            }

            Alternativa alternativa = alternativaRepository.findByIdAndQuestao(respostaAluno.getAlternativaId(), questao);
            if (alternativa == null) {
                throw new BusinessLogicException("Voce precisa escolher uma alternativa para a resposta");
            }
            if (!alternativa.getQuestao().getId().equals(respostaAluno.getQuestaoId())) {
                throw new BusinessLogicException("A alternativa enviada não pertence à questão indicada.");
            }

            Resposta resposta = new Resposta();

            if(alternativa.getCorreta()){
                acertos++;
                resposta.setStatusResposta(true);
            }
            else{
                resposta.setStatusResposta(false);
            }
            // defini atributos da questaos
            resposta.setQuestao(alternativa.getQuestao());
            resposta.setAlternativaEscolhida(alternativa);
            resposta.setQuizRespondido(quizRespondido);
            resposta.setTempoResposta(respostaAluno.getTempoResposta());

            respostasEntidade.add(resposta);
        }

        pontuacaoFinal =  10 * ((double) acertos /tamanhoQuiz);
        // Criação do objeto
        quizRespondido.setAluno(aluno);
        quizRespondido.setPontuacaoFinal(pontuacaoFinal);
        quizRespondido.setDataTentativa(LocalDateTime.now());
        quizRespondido.setQuiz(quiz);

        quizRespondido.getRespostas().addAll(respostasEntidade);

        quizRespondidoRepository.save(quizRespondido);
        return modelMapper.map(quizRespondido, QuizRespondidoDTO.class);
    }
    // TODO criar meio de calcular a pontuacao de um aluno baseada em suas respostas dos quizes
}
