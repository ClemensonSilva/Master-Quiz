package br.com.edu.ufersa.projeto_quiz.Model.repository;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    List<Aluno> findByDisciplinas(Disciplina disciplina);

    Aluno findAlunoById(Long id);

    Optional<Aluno> findByEmail(String email);
}
