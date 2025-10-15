package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTO;
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
    public ResponseEntity<List<QuizDTO>> findAll(){
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizDTO> findById(@PathVariable Long id){
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<QuizDTO> create(@RequestBody QuizDTO quizDTO){
        return new ResponseEntity<>(service.save(quizDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<QuizDTO> delete(@PathVariable Long id){
        return new ResponseEntity<>(service.delete(id), HttpStatus.OK);
    }
}
