package br.com.edu.ufersa.projeto_quiz.Model.repository;

import br.com.edu.ufersa.projeto_quiz.Model.entity.QuizRespondido;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long> {
    public List<Resposta> findByQuizRespondido(QuizRespondido quizRespondido);
    public  List<Resposta> findByQuizRespondidoAndStatusRespostaTrue(QuizRespondido quizRespondido);

}
