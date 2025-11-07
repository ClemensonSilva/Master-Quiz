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

    @Column(length = 100, nullable = false)
    private String descricao;

    // uma questao possui apenas uma alternativa correta
    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "alternativa_correta_id")
    private Alternativa alternativaCorreta;

    @OneToMany(mappedBy = "questao", cascade = CascadeType.ALL) // uma questao possui muitas alternativas
    private List<Alternativa> alternativas = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Quiz> quiz;

//    public static Questao convert(QuestaoDTO dto, Quiz quiz){
//        Questao questao = new Questao();
//        questao.setDescricao(dto.getDescricao());
//        questao.setAlternativaCorreta(Alternativa.convert(dto.getAlternativaCorreta()));
//        questao.setAlternativas(dto.getAlternativas().stream().map(q -> Alternativa.convert(q)).collect(Collectors.toList()));
//        questao.setQuiz(quiz);
//        return questao;
//    }

    public void addAlternativa(Alternativa alternativa) {
        this.alternativas.add(alternativa);
        alternativa.setQuestao(this);
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
