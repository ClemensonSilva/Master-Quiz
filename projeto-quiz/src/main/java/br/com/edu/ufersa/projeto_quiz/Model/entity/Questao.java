package br.com.edu.ufersa.projeto_quiz.Model.entity;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_questao")
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String descricao;

    @OneToMany(mappedBy = "questao", cascade = CascadeType.ALL, orphanRemoval = true) // uma questao possui muitas alternativas
    private List<Alternativa> alternativas = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Quiz> quiz;

    public void addRelQuestaoQuiz(Quiz quiz){
        this.quiz.add(quiz);
    }

    public void removeRelQuestaoQuiz(Quiz quiz){
        this.quiz.remove(quiz);
    }
    public void addAlternativa(Alternativa alternativa) {
        this.alternativas.add(alternativa);
        alternativa.setQuestao(this);
    }
    public void removeAlternativa(Alternativa alternativa) {
        this.alternativas.remove(alternativa);
        alternativa.setQuestao(null);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Questao questao = (Questao) o;
        return Objects.equals(id, questao.id);
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
