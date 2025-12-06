package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlternativaDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Alternativa;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuestaoRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuizRepository;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

    public List<QuestaoDTOResponse> findAll(long disciplinaId) throws ResourceNotFound {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(disciplinaId);
        List<Questao> questoes = repository.findAllByDisciplina(disciplina);

        if(questoes.isEmpty()){
            throw new ResourceNotFound("Não há nenhuma questão nessa disciplina.");
        }

        return questoes
                .stream()
                .map((x) -> mapper.map(x, QuestaoDTOResponse.class))
                .collect(Collectors.toList());
    }

    public QuestaoDTOResponse findById(long id){
        Optional<Questao> questao = repository.findById(id);
        if(questao.isPresent())
            return mapper.map(questao.get(), QuestaoDTOResponse.class);
        return null;
    }
//    // TODO criar um builder para ajudar na criacao da questao
//    @Transactional
//    public QuestaoDTO save(QuestaoDTO questaoDTO){
//        // verifica se o quiz existe
//        Quiz quiz = quizRepository.findById(questaoDTO.getQuizId())
//                .orElseThrow(() -> new RuntimeException("Quiz não encontrado"));
//        // criacao de uma questao para persistir no DB
//        Questao novaQuestao = new Questao();
//        novaQuestao.setDescricao(questaoDTO.getDescricao());
//        novaQuestao.setQuiz(quiz);
//
//        // associa as alternativas  à questao
//        questaoDTO.getAlternativas().forEach(alternativaDTO -> {
//            Alternativa novaAlternativa = new Alternativa();
//            novaAlternativa.setDescricao(alternativaDTO.getDescricao());
//            novaQuestao.addAlternativa(novaAlternativa);
//        });
//        // mapeia a alternativa correta
//        Alternativa alternativaCorreta = novaQuestao.getAlternativas().stream()
//                .filter(a -> a.getDescricao().equals(questaoDTO.getAlternativaCorreta().getDescricao()))
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("Alternativa correta não encontrada na lista de alternativas"));
//        novaQuestao.setAlternativaCorreta(alternativaCorreta);
//
//        Questao questao = repository.save(novaQuestao);
//        return QuestaoDTO.convert(questao);
//    }

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
                .map((x)-> mapper.map(x, QuestaoDTO.class))
                .collect(Collectors.toList());
    }

    public QuestaoDTO edit(long id,@Valid QuestaoDTO dto) throws ResourceNotFound {
        Questao questao = repository.findQuestaoById(id);

        if(questao == null ) {
            throw new ResourceNotFound("Questão não encontrada");
        }

        questao.setDescricao(dto.getDescricao());
        questao.setAlternativaCorreta(mapper.map(dto.getAlternativaCorreta(), Alternativa.class));
        questao.setAlternativas(
                dto.getAlternativas()
                        .stream()
                        .map((x) -> mapper.map(x, Alternativa.class))
                        .toList());

        return mapper.map(questao, QuestaoDTO.class);
    }
}
