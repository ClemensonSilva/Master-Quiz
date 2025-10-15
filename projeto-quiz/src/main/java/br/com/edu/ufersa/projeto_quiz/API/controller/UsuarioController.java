package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.*;
import br.com.edu.ufersa.projeto_quiz.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    @Autowired
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/alunos")
    public ResponseEntity<ReturnAlunoDTO> criarAluno(@Valid @RequestBody InputAlunoDTO alunoDTO) {
        ReturnAlunoDTO alunoSalvo = service.criarAluno(alunoDTO);
        return new ResponseEntity<>(alunoSalvo, HttpStatus.CREATED);
    }

    @PostMapping("/professores")
    public ResponseEntity<ReturnProfessorDTO> criarProfessor(@Valid @RequestBody InputProfessorDTO professorDTO) {
        ReturnProfessorDTO professorSalvo = service.criarProfessor(professorDTO);
        return new ResponseEntity<>(professorSalvo, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReturnUsuarioDTO>> listarTodos() {
        List<ReturnUsuarioDTO> usuarios = service.listarTodos();
        return ResponseEntity.ok(usuarios);
    }
}