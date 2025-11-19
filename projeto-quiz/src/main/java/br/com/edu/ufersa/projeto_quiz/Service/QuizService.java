package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.DisciplinaDTO;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizService {
    @Autowired
    QuizRepository repository;
    @Autowired
    DisciplinaRepository disciplinaRepository;
    private final ModelMapper mapper;
    @Autowired
    public QuizService(ModelMapper mapper) {
        this.mapper = mapper;
    }

    public List<QuizDTOResponse> findAll(){
        List<Quiz> quizes = repository.findAll();
        return quizes
                .stream()
                .map((x) -> mapper.map(x, QuizDTOResponse.class))
                .collect(Collectors.toList());
    }

    public QuizDTO findById(long id){
        Optional<Quiz> quiz = repository.findById(id);
        if(quiz.isPresent())
            return mapper.map(quiz.get(), QuizDTO.class);
        return null;
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
