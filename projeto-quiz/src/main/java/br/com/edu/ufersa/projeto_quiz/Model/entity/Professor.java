package br.com.edu.ufersa.projeto_quiz.Model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.Set;
import java.util.HashSet;

@Entity
@DiscriminatorValue("PROFESSOR")
public class Professor extends Usuario{

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private Set<Disciplina> disciplinas = new HashSet<>();

    public  Professor(){}

    public Set<Disciplina> getDisciplinas(){
        return disciplinas;
    }
    public void setDisciplinas(Set<Disciplina> disciplinas){
        this.disciplinas = disciplinas;
    }

}
