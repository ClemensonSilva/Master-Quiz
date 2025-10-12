package br.com.edu.ufersa.projeto_quiz.Model.entity;

import java.util.Set;

public class Quiz {
    private Set<Questao> questoes;

    public Set<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(Set<Questao> questoes) {
        this.questoes = questoes;
    }


}
