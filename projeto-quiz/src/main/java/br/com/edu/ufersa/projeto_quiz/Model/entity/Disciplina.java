package br.com.edu.ufersa.projeto_quiz.Model.entity;

import br.com.edu.ufersa.projeto_quiz.API.dto.DisciplinaDTO;
import com.fasterxml.jackson.databind.util.BeanUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_disciplina")

/**
 * Representa uma Disciplina (ou Matéria) dentro do sistema de Quizes.
 * * <p>Esta é a entidade central que agrupa Quizes e Questões e define o escopo
 * de trabalho de um Professor e a participação de Alunos.
 * *  <p> **REGRA DE DOMÍNIO:** Todas as Questões neste {@code Set} são exclusivas
 *  * desta Disciplina e não devem ser associadas a nenhuma outra Disciplina.
 * * <p>A entidade utiliza o {@code GenerationType.IDENTITY} para a chave primária,
 * delegando a criação de IDs ao banco de dados.</p>
 * * @author Franscico Clemenson
 * @version 1.0
 * @see Quiz
 * @see Questao
 * @see Professor
 * @see Aluno
 */

public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 400)
    private String descricao;

    @OneToMany(mappedBy = "disciplina",cascade = CascadeType.REMOVE)
    private List<Quiz> quizes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    @OneToMany(mappedBy = "disciplina", cascade = CascadeType.REMOVE)
    private Set<Questao> questoes;

    @ManyToMany()
    @JoinTable(
            name = "tb_disciplina_aluno",
            joinColumns = @JoinColumn(name = "disciplina_id"),
            inverseJoinColumns = @JoinColumn(name = "aluno_id")
    )
    private Set<Aluno> aluno;

    public static Disciplina convert(DisciplinaDTO dto){
        Disciplina disciplina = new Disciplina();
        BeanUtils.copyProperties(dto, disciplina);
        return disciplina;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Disciplina that = (Disciplina) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
