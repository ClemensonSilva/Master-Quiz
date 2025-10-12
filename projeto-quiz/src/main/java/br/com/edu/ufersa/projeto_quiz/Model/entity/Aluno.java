package br.com.edu.ufersa.projeto_quiz.Model.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tb_aluno")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // um aluno possui vários quizes_respondidos
    @OneToMany(mappedBy = "aluno")
    private Set<QuizRespondido> quizRespondidos;
}
