package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Service.QuizService;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/disciplinas/{disciplinaId}/quizes")
public class QuizController {

    private final QuizService service;

    @Autowired
    public QuizController(QuizService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<QuizDTOResponse>> findAll(@PathVariable Long disciplinaId) throws ResourceNotFound {
        List<QuizDTOResponse> quizList = service.findAll(disciplinaId);

        if (quizList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        for (QuizDTOResponse quiz : quizList) {
            long quizId = quiz.getId();
            quiz.add(linkTo(methodOn(QuizController.class).findById(quizId, disciplinaId)).withSelfRel());
            adicionarLinkDisciplina(quiz, disciplinaId);
        }

        return ResponseEntity.ok(quizList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTOResponse> findById(@PathVariable Long id, @PathVariable Long disciplinaId) throws ResourceNotFound {
        QuizDTOResponse quiz = service.findById(id);

        quiz.add(linkTo(methodOn(QuizController.class).findById(id, disciplinaId)).withSelfRel());

        quiz.add(linkTo(methodOn(QuizController.class).findAll(disciplinaId)).withRel("lista-quizes"));

        adicionarLinkDisciplina(quiz, disciplinaId);

        quiz.add(linkTo(methodOn(QuizController.class).delete(id, disciplinaId)).withRel("deletar"));

        return ResponseEntity.ok(quiz);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @PathVariable Long disciplinaId) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void adicionarLinkDisciplina(QuizDTOResponse quiz, Long disciplinaId) {
        Link disciplinaLink = linkTo(methodOn(DisciplinaController.class)
                .findById(disciplinaId))
                .withRel("disciplina");

        quiz.add(disciplinaLink);
    }
}