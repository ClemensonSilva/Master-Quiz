package br.com.edu.ufersa.projeto_quiz.API.dto;

import br.com.edu.ufersa.projeto_quiz.Model.entity.Usuario;

import java.util.Objects;

/**
 * Classe responsável por retornar nome e email em consultas de usuário.
 * Não há referencia a senhas nela.
 */
public abstract class ReturnUsuarioDTO {
    private Long id;
    private String nome;
    private String email;

    public ReturnUsuarioDTO(){}

    public ReturnUsuarioDTO(Usuario usuario){
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
    }

    public Long getId(){return id;}
    public void setId(Long id){this.id = id;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReturnUsuarioDTO that = (ReturnUsuarioDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
