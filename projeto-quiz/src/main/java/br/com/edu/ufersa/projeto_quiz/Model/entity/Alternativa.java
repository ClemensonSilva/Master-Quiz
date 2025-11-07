package br.com.edu.ufersa.projeto_quiz.Model.entity;

import br.com.edu.ufersa.projeto_quiz.API.dto.AlternativaDTO;
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
@Table(name = "tb_alternativa")
public class Alternativa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String descricao;

    // muitas alternativas estao associadas a uma questao
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, name = "questao_id")
    private Questao questao;

    public static Alternativa convert(AlternativaDTO alternativaDTO) {
        Alternativa alternativa = new Alternativa();
        alternativa.setId(alternativaDTO.getId());
        alternativa.setDescricao(alternativaDTO.getDescricao());
        return alternativa;
    }
}