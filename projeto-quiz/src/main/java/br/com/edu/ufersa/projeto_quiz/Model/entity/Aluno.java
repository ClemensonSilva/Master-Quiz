package br.com.edu.ufersa.projeto_quiz.Model.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tb_aluno")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aluno_id") // será usado no mapeamento feito no JPA
    private Long id;
    // um aluno possui vários quizes_respondidos
    @OneToMany()
    @JoinColumn(name = "aluno_id", nullable = false) // cria em quiz_respondido uma FK 'aluno_id' para o mapeamento
    private Set<QuizRespondido> quizRespondidos;
}
