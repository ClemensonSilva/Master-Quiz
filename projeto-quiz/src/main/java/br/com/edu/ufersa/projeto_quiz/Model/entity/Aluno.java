package br.com.edu.ufersa.projeto_quiz.Model.entity;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlunoDTO;
import jakarta.persistence.*;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Entity
@DiscriminatorValue("Aluno")

public class Aluno extends Usuario{
    // TODO corrigir relacionamento redudante entre aluno e discplina
    @ManyToMany
    @JoinTable(
            name = "tb_aluno_disciplina",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "disciplina_id")
    )
    private Set<Disciplina> disciplinas = new HashSet<>();

    public static Aluno convert(AlunoDTO dto){
        Aluno aluno = new Aluno();
        BeanUtils.copyProperties(dto, aluno);
        return aluno;
    }

    public Set<Disciplina> getDisciplinas(){
        return disciplinas;
    }
    public void setDisciplinas(Set<Disciplina> disciplinas){
        this.disciplinas = disciplinas;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_ALUNO"));
    }

}
