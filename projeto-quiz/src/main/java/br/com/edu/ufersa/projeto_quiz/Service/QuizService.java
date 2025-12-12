package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.DisciplinaDTOResponse;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTOResponse;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import br.com.edu.ufersa.projeto_quiz.Model.repository.DisciplinaRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuestaoRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuizRepository;
import br.com.edu.ufersa.projeto_quiz.exception.BusinessLogicException;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizService {
    private final QuizRepository repository;
    private final ModelMapper mapper;
    private final DisciplinaService disciplinaService;
    private  final QuestaoRepository questaoRepository;
    private final DisciplinaRepository disciplinaRepository;
    @Autowired
    public  QuizService(QuizRepository repository, ModelMapper mapper, @Lazy DisciplinaService disciplinaService, QuestaoRepository questaoRepository, DisciplinaRepository disciplinaRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.disciplinaService = disciplinaService;
        this.questaoRepository = questaoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    /**
     *  Encontra todos os quizes da disciplina
     * @param disciplinaId
     * @return quizes da disciplina
     * @throws ResourceNotFound
     */
    public List<QuizDTOResponse> findAll(long disciplinaId) throws ResourceNotFound {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(disciplinaId);
        List<Quiz> quizes = repository.findAllByDisciplina(disciplina);

        if (quizes.isEmpty()){
            throw new ResourceNotFound("Nenhum quiz encontrado para a diciplina");
        }

        return quizes
                .stream()
                .map((x) -> mapper.map(x, QuizDTOResponse.class))
                .collect(Collectors.toList());
    }

    public QuizDTOResponse findById(long id){
        Optional<Quiz> quiz = repository.findById(id);
        if(quiz.isPresent())
            return mapper.map(quiz.get(), QuizDTOResponse.class);
        return null;
    }

    public DisciplinaDTOResponse getDisciplinaByQuiz(long id) throws ResourceNotFound {
        DisciplinaDTOResponse disciplina = disciplinaService.getDisciplinaByQuiz(id);
        return disciplina;
    }

    /**
     * Método usado para salvar um quiz e associa-lo a uma disciplina.
     * Regra: nenhuma questão pode ser adicionada no momento de criação, então este método
     * é usado apenas para associar um quiz vazio com uma disciplina existente.
     * @param quizDTO
     * @param disciplina
     * @return QuizDTO
     */
    public QuizDTO save(QuizDTO quizDTO, Disciplina disciplina){
        Quiz quiz = mapper.map(quizDTO, Quiz.class);
        quiz.setDisciplina(disciplina);
        repository.save(quiz);
        return mapper.map(quiz, QuizDTO.class);
    }

    public QuizDTO delete(long id){
        Optional<Quiz> quiz = repository.findById(id);
        if(quiz.isPresent())
            repository.delete(quiz.get());
        return null;
    }

    public List<QuizDTO> findByDisciplina(Disciplina disciplina) throws ResourceNotFound {
        List<Quiz> quizes = repository.findQuizByDisciplina(disciplina);
        if (quizes.isEmpty()) throw new ResourceNotFound("Não há quizzes associados à disciplina " + disciplina.getNome());
        return quizes
                .stream()
                .map((x) -> mapper.map(x, QuizDTO.class))
                .collect(Collectors.toList());
    }

    public QuizDTOResponse update(QuizDTO quizDTO, long id, long disciplinaId) throws ResourceNotFound {
        Quiz quiz = repository.findQuizById(id);
        if (quiz == null) {
            throw new ResourceNotFound("Nenhum quiz encontrado");
        }

        quiz.setTitulo(quizDTO.getTitulo());
        return mapper.map(quiz, QuizDTOResponse.class);
    }

    public QuizDTOResponse addQuestao(long disciplinaId, long id, long questaoId) throws ResourceNotFound, BusinessLogicException {
        Disciplina disciplina = disciplinaRepository.findDisciplinaById(disciplinaId);

        if(disciplina == null){
            throw new ResourceNotFound("Disciplina não encontrada");
        }

        Quiz quiz = repository.findQuizById(id);
        if (quiz == null) {
            throw new ResourceNotFound("Nenhum quiz encontrado.");
        }
        Questao questao = questaoRepository.findQuestaoById(questaoId);
        if (questao == null) {
            throw new ResourceNotFound("Nenhuma questão encontrada.");
        }

        if(!questao.getDisciplina().equals(quiz.getDisciplina()))
            throw new BusinessLogicException("Apenas questões da disciplina podem ser adicionadas ao quiz.");

        quiz.addQuestao(questao);
        repository.save(quiz);
        return mapper.map(quiz, QuizDTOResponse.class);
    }

    public QuizDTOResponse removeQuestao(long disciplinaId, long id, long questaoId) throws ResourceNotFound, BusinessLogicException {
        Disciplina disciplina = disciplinaRepository.findDisciplinaById(disciplinaId);

        if(disciplina == null){
            throw new ResourceNotFound("Disciplina não encontrada");
        }

        Quiz quiz = repository.findQuizById(id);
        if (quiz == null) {
            throw new ResourceNotFound("Nenhum quiz encontrado.");
        }
        Questao questao = questaoRepository.findQuestaoById(questaoId);
        if (questao == null) {
            throw new ResourceNotFound("Nenhuma questão encontrada.");
        }
        if(!questao.getDisciplina().equals(quiz.getDisciplina()))
            throw new BusinessLogicException("Não é possivel remover questões que pertencem a outra disciplina.");

        quiz.removeQuestao(questao);
        repository.save(quiz);
        return mapper.map(quiz, QuizDTOResponse.class);
    }

}
