package br.com.edu.ufersa.projeto_quiz.Model.entity;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "tb_quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;

    // TODO precisa adicionar um cascadeType all
    @OneToMany(mappedBy = "quiz")
    private Set<Questao> questoes;

    @ManyToOne
    @JoinColumn(name = "disciplina_id")
    private Disciplina disciplina;

    public static Quiz convert(QuizDTO quizDTO) {
        Quiz quiz = new Quiz();
        quiz.setId(quiz.getId());
        quiz.setTitulo(quiz.getTitulo());
        quiz.setDisciplina(quiz.getDisciplina());
        quiz.setQuestoes(quiz.getQuestoes());
        return quiz;
    }
}
