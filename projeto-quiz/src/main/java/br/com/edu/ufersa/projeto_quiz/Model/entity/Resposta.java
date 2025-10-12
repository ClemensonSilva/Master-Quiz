package br.com.edu.ufersa.projeto_quiz.Model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Resposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "questao_id")
    private Questao questao;

    @ManyToOne
    @JoinColumn(nullable = false,name = "alternativa_escolhida_id")
    private Alternativa alternativaEscolhida;

    @ManyToOne
    @JoinColumn(nullable = false, name = "quiz_respondido_id")
    private QuizRespondido quizRespondido;

    @Column(name = "status_resposta")
    private boolean statusResposta;

    @Column(name = "tempo_resposta")
    private Long tempoResposta;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Resposta resposta = (Resposta) o;
        return Objects.equals(id, resposta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
