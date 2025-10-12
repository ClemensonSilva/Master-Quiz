package br.com.edu.ufersa.projeto_quiz.Model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString

public class Alternativa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String descricao;

    // muitas alternativas estao associadas a uma questao
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "questao_id")
    private Questao questao;
}
