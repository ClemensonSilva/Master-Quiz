package br.com.edu.ufersa.projeto_quiz.Model.entity;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuizRespondidoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

// um QuizRespondido pertence a um único aluno e um aluno possui 0..n QuizesRespondidos

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_quiz_respondido")
public class QuizRespondido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false)
    private Long pontuacaoFinal ;

    @Column(nullable = false, name = "data_tentativa")
    private LocalDate dataTentativa;

    // o mapeamento é feito a partir do atributo quizRespondido da entidade Resposta
    // quando uma resposta é removida da entitdade QuizRespondido, ela tambem é removida da Resposta
    // ao deletar um quizRespondido do DB, todas as respostas associadas a ele serão removidas do DB
    @OneToMany(mappedBy = "quizRespondido", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Resposta> respostas;


    public static QuizRespondido convert(QuizRespondidoDTO qrDTO){
        QuizRespondido quizRespondido = new QuizRespondido();
        quizRespondido.setAluno(qrDTO.getAluno());
        quizRespondido.setQuiz(qrDTO.getQuiz());
        quizRespondido.setPontuacaoFinal(qrDTO.getPontuacaoFinal());
        quizRespondido.setDataTentativa(qrDTO.getDataTentativa());
        quizRespondido.setRespostas(qrDTO.getRespostas());
        return quizRespondido;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QuizRespondido that = (QuizRespondido) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
