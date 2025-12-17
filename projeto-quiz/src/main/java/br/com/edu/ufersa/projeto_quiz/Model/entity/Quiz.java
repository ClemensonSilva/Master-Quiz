package br.com.edu.ufersa.projeto_quiz.Model.entity;

import br.com.edu.ufersa.projeto_quiz.API.dto.DisciplinaDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
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
    @Column(nullable = false, length = 400)
    private String descricao;
    @ManyToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private Set<Questao> questoes;

    @ManyToOne
    @JoinColumn(name = "disciplina_id")
    private Disciplina disciplina;

    public void addQuestao(Questao questao){
        this.questoes.add(questao);
    }
    public void removeQuestao(Questao questao){
        this.questoes.remove(questao);
    }


    public static Quiz convert(QuizDTO quizDTO, Disciplina disciplina, Set<Questao> questoes) {
        Quiz quiz = convert(quizDTO, disciplina);
        quiz.setQuestoes(questoes);
        return quiz;
    }
    // para salvar quando as questoes da disciplina nao estiverem criadas ainda
    public static Quiz convert(QuizDTO quizDTO, Disciplina disciplina) {
        Quiz quiz = new Quiz();
        quiz.setTitulo(quizDTO.getTitulo());
        quiz.setDisciplina(disciplina);
        return quiz;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Quiz quiz = (Quiz) o;
        return Objects.equals(id, quiz.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
