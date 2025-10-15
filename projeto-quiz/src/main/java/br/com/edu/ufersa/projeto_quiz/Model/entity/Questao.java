package br.com.edu.ufersa.projeto_quiz.Model.entity;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alternativa_correta_id")
    private Alternativa alternativaCorreta;

    @OneToMany(mappedBy = "questao") // uma questao possui muitas alternativas
    private List<Alternativa> alternativas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "quiz_id")
    private Quiz quiz;

    public static Questao convert(QuestaoDTO dto){
        Questao questao = new Questao();
        questao.setId(dto.getId());
        questao.setDescricao(dto.getDescricao());
        questao.setAlternativaCorreta(dto.getAlternativaCorreta());
        questao.setAlternativas(dto.getAlternativas());
        questao.setQuiz(dto.getQuiz());
        return questao;
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
