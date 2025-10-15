package br.com.edu.ufersa.projeto_quiz.Model.repository;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import br.com.edu.ufersa.projeto_quiz.Model.entity.QuizRespondido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuizRespondidoRepository extends JpaRepository<QuizRespondido, Long> {
    // TODO o objeto retornado deve ser um DTO
    public List<QuizRespondido> findByAluno(Aluno aluno);
    public QuizRespondido findQuizRespondidoById(Long id);
    public  QuizRespondido findByQuiz(Quiz quiz); // para encontrar todos as respostas para dado quiz
    public List<QuizRespondido> findByAlunoAndDataTentativaBetween(Aluno aluno,  LocalDate dataInicio,  LocalDate dataFim);
}
