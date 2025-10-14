package br.com.edu.ufersa.projeto_quiz.Model.entity;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlunoDTO;
import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@DiscriminatorValue("ALUNO")
public class Aluno extends Usuario{

    @ManyToMany
    @JoinTable(
            name = "tb_aluno_disciplina",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "disciplina_id")
    )
    private Set<Disciplina> disciplinas = new HashSet<>();

    public Aluno(){}

    public static Aluno convert(AlunoDTO alunoDTO){
        Aluno aluno = new Aluno();
        aluno.setId(alunoDTO.getId());
        aluno.setNome(alunoDTO.getNome());
        aluno.setEmail(alunoDTO.getEmail());
        aluno.setDisciplinas(new HashSet<>());
        return aluno;
    }
    public Set<Disciplina> getDisciplinas(){
        return disciplinas;
    }
    public void setDisciplinas(Set<Disciplina> disciplinas){
        this.disciplinas = disciplinas;
    }
}
