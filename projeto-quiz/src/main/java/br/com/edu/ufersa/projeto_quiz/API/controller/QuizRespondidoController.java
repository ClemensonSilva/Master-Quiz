package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuizRespondidoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizRespondidoInputDTO;
import br.com.edu.ufersa.projeto_quiz.Service.QuizRespondidoService;
import br.com.edu.ufersa.projeto_quiz.exception.BusinessLogicException;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/disciplinas/{disciplinaId}/quizes/{quizId}")
public class QuizRespondidoController {
    @Autowired
    private QuizRespondidoService  quizRespondidoService;

   // TODO Metodo para puxar todas as tentativas de responder um quiz por aluno
    @PostMapping()
    public ResponseEntity<QuizRespondidoDTO> responderQuiz(@RequestBody QuizRespondidoInputDTO dto,
                                                           @PathVariable long quizId,
                                                           @PathVariable long disciplinaId) throws ResourceNotFound, BusinessLogicException {

        return new ResponseEntity<>(quizRespondidoService.responderQuiz(dto, quizId), HttpStatus.CREATED);
    }
}
