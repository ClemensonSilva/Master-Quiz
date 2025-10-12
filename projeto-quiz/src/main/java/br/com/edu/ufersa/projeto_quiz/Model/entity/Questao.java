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
@Table(name = "tb_questao")
public class Questao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private  String descricao;

    // uma questao possui apenas uma alternativa correta
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alternativa_correta_id")
    private Alternativa alternativaCorreta;

    @OneToMany(mappedBy = "questao") // uma questao possui muitas alternativas
    private List<Alternativa> alternativas;
    @ManyToOne(fetch = FetchType.LAZY)

    @JoinColumn(nullable = false, name = "quiz_id")
    private Quiz quiz;
}
