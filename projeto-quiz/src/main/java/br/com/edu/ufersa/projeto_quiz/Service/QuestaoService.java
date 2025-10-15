package br.com.edu.ufersa.projeto_quiz.Service;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTO;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Questao;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Quiz;
import br.com.edu.ufersa.projeto_quiz.Model.repository.QuestaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestaoService {
    @Autowired
    private QuestaoRepository repository;

    public List<QuestaoDTO> findAll(){
        List<Questao> questoes = repository.findAll();
        return questoes
                .stream()
                .map(QuestaoDTO::convert)
                .collect(Collectors.toList());
    }

    public QuestaoDTO findById(long id){
        Optional<Questao> questao = repository.findById(id);
        if(questao.isPresent())
            return QuestaoDTO.convert(questao.get());
        return null;
    }

    public QuestaoDTO save(QuestaoDTO questaoDTO){
        Questao questao = repository.save(Questao.convert(questaoDTO));
        return QuestaoDTO.convert(questao);
    }

    public QuestaoDTO delete(long id){
        Optional<Questao> questao = repository.findById(id);
        if(questao.isPresent())
            repository.delete(questao.get());
        return null;
    }

    public List<QuestaoDTO> findByQuiz(Quiz quiz){
        List<Questao> questoes = repository.findQuestoesByQuiz(quiz);
        return questoes
                .stream()
                .map(QuestaoDTO::convert)
                .collect(Collectors.toList());
    }
}
