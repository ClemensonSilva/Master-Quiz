package br.com.edu.ufersa.projeto_quiz.Model.entity;

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

    public Set<Disciplina> getDisciplinas(){
        return disciplinas;
    }
    public void setDisciplinas(Set<Disciplina> disciplinas){
        this.disciplinas = disciplinas;
    }
}
