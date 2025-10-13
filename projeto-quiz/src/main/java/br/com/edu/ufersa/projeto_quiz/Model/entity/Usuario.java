package br.com.edu.ufersa.projeto_quiz.Model.entity;

import jakarta.persistence.*;
import org.hibernate.sql.exec.spi.StandardEntityInstanceResolver;

import javax.annotation.processing.Generated;
import java.util.HashSet;

@Entity
@Table (name="tb_usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Chave primária auto incrementada
    @Column(unique = true, nullable = false)
    private String nome;
    @Column(unique = true, nullable = false)
    private String endEmail;

    
    private Set<Disciplina> disciplinas = new HashSet<>();

}
