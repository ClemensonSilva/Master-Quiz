package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuizRespondidoDTO;
import br.com.edu.ufersa.projeto_quiz.Service.QuizRespondidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/quizes-respondidos")
public class QuizRespondidoController {
    @Autowired
    private QuizRespondidoService  quizRespondidoService;

    // TODO adiciona elemento de paginacao
    @GetMapping()
    public ResponseEntity<List<QuizRespondidoDTO>> findAll() {
        return new  ResponseEntity<List<QuizRespondidoDTO>>(quizRespondidoService.findAll(), HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<QuizRespondidoDTO> save(@RequestBody QuizRespondidoDTO dto) {

        return new ResponseEntity<>(quizRespondidoService.createQuiz(dto), HttpStatus.CREATED);
    }
}
