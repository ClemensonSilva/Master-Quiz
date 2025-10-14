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
    public QuizRespondido findQuizRespondidoByAluno(Aluno aluno);
    public QuizRespondido findQuizRespondidoById(Long id);
    public  QuizRespondido findQuizRespondidoByQuiz(Quiz quiz); // para encontrar todos as respostas para dado quiz
    @Query(value = "from QuizRespondido where aluno_id = :aluno and data_tentativa >= :data_inicio and data_tentativa <= data_fim ")
    public List<QuizRespondido> findQuizRespondidoByAlunoBetweenDates(@Param("aluno")Long alunoId, @Param("data_inicio") LocalDate dataInicio, @Param("data_fim") LocalDate dataFim);
}
