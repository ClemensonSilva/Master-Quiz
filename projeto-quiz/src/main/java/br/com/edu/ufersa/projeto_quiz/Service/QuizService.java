package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.DisciplinaDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.DisciplinaDTOResponse;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import br.com.edu.ufersa.projeto_quiz.Model.repository.DisciplinaRepository;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuizRepository;
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
    public final QuizRepository repository;
    private final ModelMapper mapper;
    public final DisciplinaService disciplinaService;
    @Autowired
    public  QuizService(QuizRepository repository, ModelMapper mapper, @Lazy DisciplinaService disciplinaService) {
        this.repository = repository;
        this.mapper = mapper;
        this.disciplinaService = disciplinaService;
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
}
