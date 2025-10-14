package br.com.edu.ufersa.projeto_quiz.Model.repository;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    public Disciplina findDisciplinaById(Long id);
    public List<Disciplina> findDisciplinaByAluno(Aluno a);
    public List<Disciplina> findDisciplinaByProfessor(Professor p);
}
