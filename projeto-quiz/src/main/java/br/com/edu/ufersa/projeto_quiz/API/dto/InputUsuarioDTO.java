package br.com.edu.ufersa.projeto_quiz.API.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Classe abstrata usada para passagem de dados para cadastro de usuários, professor ou aluno, na plataforma.
 */
public abstract class InputUsuarioDTO {
    @NotBlank(message = "O nome enviado não é válido")
    private String nome;

    @NotBlank
    @Email(message = "O email enviado não é válido")
    private String email;

    @NotBlank
    @Size(min = 5, max = 25, message = "A senha deve ter entre 5 a 25 caracteres")
    private String senha;

    public String getNome(){return nome;}
    public void setNome(String nome){this.nome = nome;}

    public String getEmail(){return email;}
    public void setEmail(String email) {this.email = email;}

    public String getSenha(){return senha;}
    public void setSenha(String senha) {this.senha = senha;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputUsuarioDTO that = (InputUsuarioDTO) o;
        return Objects.equals(email, that.email) && Objects.equals(senha, that.senha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, senha);
    }
}
