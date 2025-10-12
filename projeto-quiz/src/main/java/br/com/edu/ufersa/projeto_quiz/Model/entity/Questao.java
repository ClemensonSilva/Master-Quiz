package br.com.edu.ufersa.projeto_quiz.Model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor

public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // uma questao possui apenas uma alternativa correta
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alternativa_correta_id")
    private Alternativa alternativaCorreta;

    @OneToMany(mappedBy = "questao") // uma questao possui muitas alternativas
    private List<Alternativa> alternativas;
}
