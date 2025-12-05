package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.DisciplinaDTOResponse;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Model.entity.Disciplina;
import br.com.edu.ufersa.projeto_quiz.Service.DisciplinaService;
import br.com.edu.ufersa.projeto_quiz.Service.QuizService;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/quizes")
public class QuizController {

    private  final QuizService service;
    @Autowired
    public QuizController(QuizService service ) {
        this.service = service;
    }


    @GetMapping()
    public ResponseEntity<List<QuizDTOResponse>> findAll() throws ResourceNotFound {
        List<QuizDTOResponse> quizDTOResponse = service.findAll();

        for (QuizDTOResponse quizResponse: quizDTOResponse){
            // Passa a disciplina de cada quiz
            Link disciplinaLink = linkTo(methodOn(DisciplinaController.class).findById(service.getDisciplinaByQuiz(quizResponse.getId()).getId())).withRel("disciplina");
            quizResponse.add(disciplinaLink);
        }
        return ResponseEntity.ok(quizDTOResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTOResponse> findById(@PathVariable Long id) throws ResourceNotFound {
        QuizDTOResponse quizDTOResponse = service.findById(id);
        Link disciplinaLink = linkTo(methodOn(DisciplinaController.class).findById(service.getDisciplinaByQuiz(quizDTOResponse.getId()).getId())).withRel("disciplina");
        quizDTOResponse.add(disciplinaLink);
        return ResponseEntity.ok(quizDTOResponse);
    }

    // Não é possível criar um quiz não associados a uma disciplina
//    @PostMapping()
//    public ResponseEntity<QuizDTO> create(@RequestBody QuizDTO quizDTO){
//        return new ResponseEntity<>(service.save(quizDTO), HttpStatus.CREATED);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<QuizDTO> delete(@PathVariable Long id){
        return new ResponseEntity<>(service.delete(id), HttpStatus.OK);
    }
}
