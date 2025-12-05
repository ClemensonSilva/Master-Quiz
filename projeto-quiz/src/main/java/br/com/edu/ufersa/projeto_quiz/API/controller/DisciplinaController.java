package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.*;
import br.com.edu.ufersa.projeto_quiz.Service.DisciplinaService;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/disciplinas")
public class DisciplinaController {
    @Autowired
    private DisciplinaService service;

// TODO implementar os outros controllers dos demais servicos que estao em serviceDisciplica
   @GetMapping()
    public ResponseEntity<List<DisciplinaDTOResponse>> findAll(){
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaDTOResponse> findById(@PathVariable Long id){
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/quizes")
    public ResponseEntity<List<QuizDTO>> getQuizesByDisciplina(@PathVariable long id) throws ResourceNotFound {
        return new ResponseEntity<>(service.getQuizesByDisciplina(id), HttpStatus.OK);
    }
    @GetMapping("/{id}/alunos")
    public ResponseEntity<List<ReturnAlunoDTO>> getAlunosByDisciplina(@PathVariable long id) throws ResourceNotFound {
        return new ResponseEntity<>(service.getAlunosByDisciplina(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/professores")
    public ResponseEntity<ReturnProfessorDTO> getProfessoresByDisciplina(@PathVariable long id) throws ResourceNotFound {
        return new ResponseEntity<>(service.getProfessorByDisciplina(id), HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<DisciplinaDTO> create(@RequestBody DisciplinaDTO disciplinaDTO){
        return new ResponseEntity<>(service.save(disciplinaDTO), HttpStatus.CREATED);
    }
    @PostMapping("/{id}/quizes")
    public ResponseEntity<QuizDTO> addQuiz(@RequestBody QuizDTO quizDTO, @PathVariable long id) throws ResourceNotFound {
        return  new ResponseEntity<>(service.addQuiz(quizDTO, id), HttpStatus.CREATED);
    }
    @PostMapping("/{id}/questoes")
    public ResponseEntity<QuestaoDTOResponse> addQuestao(@RequestBody QuestaoDTO questaoDTO, @PathVariable long id) throws ResourceNotFound {
        return  new ResponseEntity<>(service.addQuestao(questaoDTO, id), HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> delete(@PathVariable Long id){
        return new ResponseEntity<>(service.delete(id), HttpStatus.OK);
    }
}
