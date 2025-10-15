package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.DisciplinaDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTO;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizService {
    @Autowired
    QuizRepository repository;

    public List<QuizDTO> findAll(){
        List<Quiz> quizes = repository.findAll();
        return quizes
                .stream()
                .map(QuizDTO::convert)
                .collect(Collectors.toList());
    }

    public QuizDTO findById(long id){
        Optional<Quiz> quiz = repository.findById(id);
        if(quiz.isPresent())
            return QuizDTO.convert(quiz.get());
        return null;
    }

    public QuizDTO save(QuizDTO quizDTO){
        Quiz quiz = repository.save(Quiz.convert(quizDTO));
        return QuizDTO.convert(quiz);
    }

    public QuizDTO delete(long id){
        Optional<Quiz> quiz = repository.findById(id);
        if(quiz.isPresent())
            repository.delete(quiz.get());
        return null;
    }

    public List<QuizDTO> findByDisciplina(Disciplina disciplina){
        List<Quiz> quizes = repository.findQuizByDisciplina(disciplina);
        return quizes
                .stream()
                .map(QuizDTO::convert)
                .collect(Collectors.toList());
    }
}
