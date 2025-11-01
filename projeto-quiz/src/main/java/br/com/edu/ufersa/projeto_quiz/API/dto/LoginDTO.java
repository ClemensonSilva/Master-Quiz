package br.com.edu.ufersa.projeto_quiz.API.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * Classe usada para login do usuário, professor ou aluno;
 * Contém informações de email e senha para login
 * @author Clemerson
 */
public class LoginDTO {
    @NotBlank
    @Email(message = "O email enviado não é válido")
    private String email;
    @NotBlank
    private String senha;

    public LoginDTO() {
    }


}