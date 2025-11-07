package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Alternativa;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuestaoRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuizRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestaoService {
    @Autowired
    private QuestaoRepository repository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private ModelMapper mapper;

    public List<QuestaoDTOResponse> findAll(){
        List<Questao> questoes = repository.findAll();
        return questoes
                .stream()
                .map((x) -> mapper.map(x, QuestaoDTOResponse.class))
                .collect(Collectors.toList());
    }

    public QuestaoDTO findById(long id){
        Optional<Questao> questao = repository.findById(id);
        if(questao.isPresent())
            return mapper.map(questao.get(), QuestaoDTO.class);
        return null;
    }
    // TODO criar um builder para ajudar na criacao da questao
    @Transactional
    public QuestaoDTO save(QuestaoDTO questaoDTO){
        // verifica se o quiz existe
        Quiz quiz = quizRepository.findById(questaoDTO.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz não encontrado"));
        // criacao de uma questao para persistir no DB
        Questao novaQuestao = new Questao();
        novaQuestao.setDescricao(questaoDTO.getDescricao());
        novaQuestao.setQuiz(quiz);

        // associa as alternativas  à questao
        questaoDTO.getAlternativas().forEach(alternativaDTO -> {
            Alternativa novaAlternativa = new Alternativa();
            novaAlternativa.setDescricao(alternativaDTO.getDescricao());
            novaQuestao.addAlternativa(novaAlternativa);
        });
        // mapeia a alternativa correta
        Alternativa alternativaCorreta = novaQuestao.getAlternativas().stream()
                .filter(a -> a.getDescricao().equals(questaoDTO.getAlternativaCorreta().getDescricao()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Alternativa correta não encontrada na lista de alternativas"));
        novaQuestao.setAlternativaCorreta(alternativaCorreta);

        Questao questao = repository.save(novaQuestao);
        return QuestaoDTO.convert(questao);
    }

    public QuestaoDTO delete(long id){
        Optional<Questao> questao = repository.findById(id);
        if(questao.isPresent())
            repository.delete(questao.get());
        return null;
    }

    public List<QuestaoDTO> findByQuiz(Quiz quiz){
        List<Questao> questoes = repository.findQuestoesByQuiz(quiz);
        return questoes
                .stream()
                .map(QuestaoDTO::convert)
                .collect(Collectors.toList());
    }
}
