package br.com.edu.ufersa.projeto_quiz.Model.repository;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestaoRepository extends JpaRepository<Questao, Long> {
     Questao findQuestaoById(Long id);
     List<Questao> findQuestoesByQuiz(Quiz q);
     List<Questao> findAllByDisciplina(Disciplina disciplina);

    Questao findQuestaoByIdAndDisciplina(long questaoId, Disciplina disciplina);
}
