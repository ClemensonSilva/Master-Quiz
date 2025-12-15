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

/**
 * Serviço responsável pela gestão de quizzes no sistema.
 * <p>
 * Esta classe contempla regras de negócio relacionadas a:
 * <ul>
 *     <li>Busca de quizzes por disciplina</li>
 *     <li>Criação e edição de quizzes</li>
 *     <li>Associação e remoção de questões de um quiz</li>
 *     <li>Associação entre quiz e disciplina</li>
 * </ul>
 * </p>
 * Todos os métodos retornam DTOs apropriados para comunicação com os controllers.
 */
@Service
public class QuizService {

    private final QuizRepository repository;
    private final ModelMapper mapper;
    private final DisciplinaService disciplinaService;
    private final QuestaoRepository questaoRepository;
    private final DisciplinaRepository disciplinaRepository;

    /**
     * Construtor do serviço de Quiz.
     *
     * @param repository           repositório de quizzes
     * @param mapper               objeto para conversão entre entidades e DTOs
     * @param disciplinaService    serviço para operações relacionadas à disciplina
     * @param questaoRepository    repositório de questões
     * @param disciplinaRepository repositório de disciplinas
     */
    @Autowired
    public QuizService(QuizRepository repository, ModelMapper mapper, @Lazy DisciplinaService disciplinaService,
                       QuestaoRepository questaoRepository, DisciplinaRepository disciplinaRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.disciplinaService = disciplinaService;
        this.questaoRepository = questaoRepository;
        this.disciplinaRepository = disciplinaRepository;
    }

    /**
     * Encontra todos os quizzes associados a uma disciplina.
     *
     * @param disciplinaId ID da disciplina
     * @return lista de quizzes da disciplina em formato DTO
     * @throws ResourceNotFound caso nenhuma disciplina ou quiz sejam encontrados
     */
    public List<QuizDTOResponse> findAll(long disciplinaId) throws ResourceNotFound {
        Disciplina disciplina = new Disciplina();
        disciplina.setId(disciplinaId);
        List<Quiz> quizes = repository.findAllByDisciplina(disciplina);

        if (quizes.isEmpty()) {
            throw new ResourceNotFound("Nenhum quiz encontrado para a disciplina");
        }

        return quizes.stream()
                .map(x -> mapper.map(x, QuizDTOResponse.class))
                .collect(Collectors.toList());
    }

    /**
     * Busca um quiz pelo ID.
     *
     * @param id ID do quiz
     * @return DTO contendo informações do quiz ou null caso não exista
     */
    public QuizDTOResponse findById(long id) {
        Optional<Quiz> quiz = repository.findById(id);
        return quiz.map(value -> mapper.map(value, QuizDTOResponse.class)).orElse(null);
    }

    /**
     * Obtém a disciplina associada a um quiz específico.
     *
     * @param id ID do quiz
     * @return DTO da disciplina vinculada ao quiz
     * @throws ResourceNotFound caso o quiz não esteja associado a uma disciplina
     */
    public DisciplinaDTOResponse getDisciplinaByQuiz(long id) throws ResourceNotFound {
        return disciplinaService.getDisciplinaByQuiz(id);
    }


    /**
     * Salva um novo quiz e associa-o a uma disciplina.
     * <p>
     * Regra: um quiz não pode ser criado com questões. Este método apenas
     * cria um quiz vazio vinculado a uma disciplina existente.
     * </p>
     *
     * @param quizDTO    dados do quiz
     * @param disciplina disciplina à qual o quiz será vinculado
     * @return DTO do quiz criado
     */
    public QuizDTO save(QuizDTO quizDTO, Disciplina disciplina) {
        Quiz quiz = mapper.map(quizDTO, Quiz.class);
        quiz.setDisciplina(disciplina);
        repository.save(quiz);
        return mapper.map(quiz, QuizDTO.class);
    }

    /**
     * Remove um quiz pelo ID.
     *
     * @param id ID do quiz
     * @return sempre null após remover
     */
    public QuizDTO delete(long id) {
        Optional<Quiz> quiz = repository.findById(id);
        quiz.ifPresent(repository::delete);
        return null;
    }

    /**
     * Busca quizzes associados a uma disciplina.
     *
     * @param disciplina entidade de disciplina
     * @return lista de quizzes convertidos para DTO
     * @throws ResourceNotFound caso a disciplina não possua quizzes
     */
    public List<QuizDTO> findByDisciplina(Disciplina disciplina) throws ResourceNotFound {
        List<Quiz> quizes = repository.findQuizByDisciplina(disciplina);
        if (quizes.isEmpty())
            throw new ResourceNotFound("Não há quizzes associados à disciplina " + disciplina.getNome());

        return quizes.stream()
                .map(x -> mapper.map(x, QuizDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Atualiza informações de um quiz existente.
     *
     * @param quizDTO      dados para atualização
     * @param id           ID do quiz
     * @param disciplinaId ID da disciplina (não utilizado no momento)
     * @return DTO atualizado do quiz
     * @throws ResourceNotFound caso o quiz não exista
     */
    public QuizDTOResponse update(QuizDTO quizDTO, long id, long disciplinaId) throws ResourceNotFound {
        Quiz quiz = repository.findQuizById(id);
        if (quiz == null) {
            throw new ResourceNotFound("Nenhum quiz encontrado");
        }

        quiz.setTitulo(quizDTO.getTitulo());
        repository.save(quiz);
        return mapper.map(quiz, QuizDTOResponse.class);
    }

    /**
     * Adiciona uma questão a um quiz.
     *
     * <p><b>Regra:</b> só é permitido adicionar questões da mesma disciplina do quiz.</p>
     *
     * @param disciplinaId ID da disciplina
     * @param id           ID do quiz
     * @param questaoId    ID da questão a adicionar
     * @return DTO do quiz atualizado
     * @throws ResourceNotFound       caso quiz, disciplina ou questão não existam
     * @throws BusinessLogicException caso a questão não pertença à disciplina do quiz
     */
    public QuizDTOResponse addQuestao(long disciplinaId, long id, long questaoId)
            throws ResourceNotFound, BusinessLogicException {

        Disciplina disciplina = disciplinaRepository.findDisciplinaById(disciplinaId);
        if (disciplina == null)
            throw new ResourceNotFound("Disciplina não encontrada");

        Quiz quiz = repository.findQuizById(id);
        if (quiz == null)
            throw new ResourceNotFound("Nenhum quiz encontrado.");

        Questao questao = questaoRepository.findQuestaoById(questaoId);
        if (questao == null)
            throw new ResourceNotFound("Nenhuma questão encontrada.");

        if (!questao.getDisciplina().equals(quiz.getDisciplina()))
            throw new BusinessLogicException("Apenas questões da disciplina podem ser adicionadas ao quiz.");

        questao.addRelQuestaoQuiz(quiz);
        questaoRepository.save(questao);
        return mapper.map(quiz, QuizDTOResponse.class);
    }

    /**
     * Remove uma questão de um quiz, respeitando regras de integridade.
     *
     * @param disciplinaId ID da disciplina
     * @param id           ID do quiz
     * @param questaoId    ID da questão
     * @return DTO do quiz atualizado após remoção
     * @throws ResourceNotFound       caso qualquer entidade não exista
     * @throws BusinessLogicException se a questão não pertencer à disciplina do quiz
     */
    public QuizDTOResponse removeQuestao(long disciplinaId, long id, long questaoId)
            throws ResourceNotFound, BusinessLogicException {

        Disciplina disciplina = disciplinaRepository.findDisciplinaById(disciplinaId);
        if (disciplina == null)
            throw new ResourceNotFound("Disciplina não encontrada");

        Quiz quiz = repository.findQuizById(id);
        if (quiz == null)
            throw new ResourceNotFound("Nenhum quiz encontrado.");

        Questao questao = questaoRepository.findQuestaoById(questaoId);
        if (questao == null)
            throw new ResourceNotFound("Nenhuma questão encontrada.");

        if (!questao.getDisciplina().equals(quiz.getDisciplina()))
            throw new BusinessLogicException("Não é possível remover questões que pertencem a outra disciplina.");

        quiz.removeQuestao(questao);
        repository.save(quiz);
        return mapper.map(quiz, QuizDTOResponse.class);
    }
}
