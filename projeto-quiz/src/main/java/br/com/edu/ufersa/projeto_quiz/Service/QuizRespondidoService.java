package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuizRespondidoDTO;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import br.com.edu.ufersa.projeto_quiz.Model.entity.QuizRespondido;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuizRespondidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizRespondidoService {
    @Autowired
    private QuizRespondidoRepository quizRespondidoRepository;

    public QuizRespondidoDTO save(QuizRespondidoDTO quizRespondidoDTO) {
        QuizRespondido quizRespondido = new QuizRespondido();
        quizRespondido.convert(quizRespondidoDTO);
        quizRespondidoRepository.save(quizRespondido);
        return quizRespondidoDTO.convert(quizRespondido);
    }
    // TODO ver com mais detalhes o PageRequest
    public List<QuizRespondidoDTO> findAll(PageRequest pageRequest) {
         return quizRespondidoRepository.findAll()
                 .stream()
                 .map(x->QuizRespondidoDTO.convert(x)).
                 collect(Collectors.toList());
    }
    // TODO preciso que o QuizDTO seja implementado para prosseguir
//    public List<QuizRespondidoDTO> findAllByQuiz(Quiz quiz) {
//        return quizRespondidoRepository.findQuizRespondidoByQuiz(quiz)
//    }

    // TODO preciso que a classe Aluno seja implementada antes de continuar
//    public List<QuizRespondidoDTO> findByDataTentativa(Aluno aluno, QuizRespondidoDTO quizRespondidoDTO) {
//        return quizRespondidoRepository.findQuizRespondidoByAlunoBetweenDates(aluno);
//    }


}
