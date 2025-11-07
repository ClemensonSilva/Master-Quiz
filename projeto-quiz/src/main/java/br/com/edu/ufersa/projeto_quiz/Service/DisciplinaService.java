package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.*;
import br.com.edu.ufersa.projeto_quiz.Model.entity.*;
import br.com.edu.ufersa.projeto_quiz.Model.repository.DisciplinaRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.ProfessorRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuestaoRepository;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DisciplinaService {
    private final DisciplinaRepository repository;
    private final ProfessorRepository professorRepository;
    private final QuestaoRepository questaoRepository;
    private final ModelMapper mapper;
    private final QuizService quizService;

    @Autowired
    public DisciplinaService(DisciplinaRepository repository, ProfessorRepository professorRepository, ModelMapper mapper, QuizService quizService, QuestaoRepository questaoRepository) {
        this.repository = repository;
        this.professorRepository = professorRepository;
        this.mapper = mapper;
        this.quizService = quizService;
        this.questaoRepository = questaoRepository;
    }

    public List<DisciplinaDTOResponse> findAll(){
        List<Disciplina> disciplinas = repository.findAll();

        return disciplinas
                .stream()
                .map((x) -> mapper.map(x, DisciplinaDTOResponse.class))
                .collect(Collectors.toList());
    }

    public DisciplinaDTOResponse findById(long id){
        Optional<Disciplina> disciplina = repository.findById(id);
        if(disciplina.isPresent())
            return mapper.map(disciplina.get(), DisciplinaDTOResponse.class);
        return null;
    }

    /**
     * Método para associar um quiz a uma disciplina. Usada o método save do service de Quiz intermante.
     * Regra:
     * @param quizDTO
     * @param disciplinaId
     * @return QuizDTO
     * @throws ResourceNotFound
     */
    public QuizDTO addQuiz(QuizDTO quizDTO, long disciplinaId) throws ResourceNotFound {
        Disciplina  disciplina = repository.findById(disciplinaId).orElseThrow(()-> new ResourceNotFound("Disciplina não encontrada"));
        return quizService.save(quizDTO, disciplina);
    }

    public DisciplinaDTO save(DisciplinaDTO disciplinaDTO){
        Professor professor = professorRepository.findById(disciplinaDTO.getProfessorId())
                .orElseThrow(() -> new DataIntegrityViolationException("Professor nao cadastrado"));

        Disciplina disciplina = Disciplina.convert(disciplinaDTO);
        disciplina.setProfessor(professor);
        disciplina = repository.save(disciplina);
        return disciplinaDTO;
    }

    public DisciplinaDTO delete(long id){
        Optional<Disciplina> disciplina = repository.findById(id);
        if(disciplina.isPresent())
            repository.delete(disciplina.get());
        return null;
    }

    public List<DisciplinaDTO> findByAluno(Aluno aluno){
        List<Disciplina> disciplinas = repository.findDisciplinaByAluno(aluno);
        return disciplinas
                .stream()
                .map((x) -> mapper.map(x, DisciplinaDTO.class))
                .collect(Collectors.toList());
    }

    public List<DisciplinaDTO> findByProfessor(Professor professor){
        List<Disciplina> disciplinas = repository.findDisciplinaByProfessor(professor);
        return disciplinas
                .stream()
                .map((x) -> mapper.map(x, DisciplinaDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Método responsável pela persistência de questões dentro do escopo de uma disciplina
     * @param questaoDTO, Data Transfer contendo informações sobre a questão
     * a alternativa correta e a lista de alternativas
     * @param disciplinaId O identificador único da Disciplina à qual a Questão pertence.
     * @return Um {@code QuestaoDTOResponse} representando a Questão recém-criada,
     * incluindo o ID gerado e dados mapeados para resposta.
     * @throws ResourceNotFound Se a Disciplina com o ID fornecido não for encontrada
     * no repositório.
     *  */
    public QuestaoDTOResponse addQuestao(QuestaoDTO questaoDTO, long disciplinaId) throws ResourceNotFound {
        Disciplina disciplina = repository.findById(disciplinaId)
                .orElseThrow(() -> new ResourceNotFound("Disciplina não encontrada"));

        Questao questao = mapper.map(questaoDTO, Questao.class);

        Alternativa alternativaCorreta = questao.getAlternativaCorreta();
        alternativaCorreta.setQuestao(questao);

        for (Alternativa alternativa : questao.getAlternativas()) {
            alternativa.setQuestao(questao);
        }

        questao.setDisciplina(disciplina);

         questaoRepository.save(questao);
        return mapper.map(questao, QuestaoDTOResponse.class);
    }
}
