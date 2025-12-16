package br.com.edu.ufersa.projeto_quiz.Model.repository;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Aluno;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Professor;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

    public List<Disciplina> findDisciplinaByAluno(Aluno a);
    public List<Disciplina> findDisciplinaByProfessor(Professor p);
    public Disciplina findDisciplinaByQuizes(Quiz quiz);

    Disciplina findDisciplinaById(Long id);

    Disciplina findDisciplinaByNomeLike(String nome);
    @Query("""
       SELECT d
       FROM Disciplina d
       LEFT JOIN d.aluno a WITH a.id = :alunoId
       WHERE a IS NULL
    """)
    List<Disciplina> findDisciplinasNaoPertencentesAoAluno(@Param("alunoId") Long alunoId);

    @Query("""
    select d
    from Disciplina d
    where :aluno member of d.aluno
      and lower(d.nome) like lower(concat('%', :nome, '%'))
""")
    List<Disciplina> searchDisciplinaByAlunoAndNome(
            @Param("aluno") Aluno aluno,
            @Param("nome") String nome
    );


    @Query("""
    select d
    from Disciplina d
    where d.professor = :professor
      and lower(d.nome) like lower(concat('%', :nome, '%'))
""")
    List<Disciplina> searchDisciplinaByProfessorAndNome(
            @Param("professor") Professor professor,
            @Param("nome") String nome
    );


}
