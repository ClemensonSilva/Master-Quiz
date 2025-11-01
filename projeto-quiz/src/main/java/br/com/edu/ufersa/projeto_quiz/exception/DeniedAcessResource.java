package br.com.edu.ufersa.projeto_quiz.exception;

public class DeniedAcessResource extends RuntimeException {
    public DeniedAcessResource(String message) {
        super(message);
    }
}
