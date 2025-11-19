package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quizes")
public class QuizController {
    @Autowired
    private QuizService service;

    @GetMapping()
    public ResponseEntity<List<QuizDTOResponse>> findAll(){
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> findById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
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
