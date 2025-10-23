package br.com.edu.ufersa.projeto_quiz.API.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    private String email;
    private String senha;

    public LoginDTO() {
    }


}