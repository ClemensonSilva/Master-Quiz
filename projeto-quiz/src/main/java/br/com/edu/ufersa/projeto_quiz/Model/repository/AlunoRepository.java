package br.com.edu.ufersa.projeto_quiz.Model.repository;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    List<Aluno> findByDisciplinas_Nome(String nomeDisciplina);
}
