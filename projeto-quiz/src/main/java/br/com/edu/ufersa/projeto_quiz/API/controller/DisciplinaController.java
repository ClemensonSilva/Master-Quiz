package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.DisciplinaDTO;
import br.com.edu.ufersa.projeto_quiz.Service.DisciplinaService;
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

    @GetMapping()
    public ResponseEntity<List<DisciplinaDTO>> findAll(){
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> findById(@PathVariable Long id){
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<DisciplinaDTO> create(@RequestBody DisciplinaDTO disciplinaDTO){
        return new ResponseEntity<>(service.save(disciplinaDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DisciplinaDTO> delete(@PathVariable Long id){
        return new ResponseEntity<>(service.delete(id), HttpStatus.OK);
    }
}
