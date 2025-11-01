package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.*;
import br.com.edu.ufersa.projeto_quiz.Service.UsuarioService;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
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


    @GetMapping("/alunos")
    public ResponseEntity<List<ReturnAlunoDTO>> listarTodosAlunos() {
        List<ReturnAlunoDTO> alunos = service.listarTodosAlunos();
        return ResponseEntity.ok(alunos);
    }
    @GetMapping("/professores")
    public ResponseEntity<List<ReturnProfessorDTO>> listarTodosProfessores() {
        List<ReturnProfessorDTO> professores = service.listarTodosProfessores();
        return ResponseEntity.ok(professores);
    }

    @GetMapping("/alunos/{id}")
    public ResponseEntity<ReturnAlunoDTO> getAluno(@PathVariable Long id) throws ResourceNotFound {
        ReturnAlunoDTO aluno = service.getAluno(id);
        return ResponseEntity.ok(aluno);
    }
    @GetMapping("/professores/{id}")
    public ResponseEntity<ReturnProfessorDTO> getProfessor(@PathVariable Long id) throws ResourceNotFound {
        ReturnProfessorDTO professor = service.getProfessor(id);
        return ResponseEntity.ok(professor);
    }

    @GetMapping("/professores/{id}/disciplinas")
    public ResponseEntity<List<DisciplinaDTOResponse>> getDisciplinasByProfessor(@PathVariable Long id) throws ResourceNotFound {
        List<DisciplinaDTOResponse> disciplinas = service.disciplinasByUser(id);
        return ResponseEntity.ok(disciplinas);
    }
    @GetMapping("/alunos/{id}/disciplinas")
    public ResponseEntity<List<DisciplinaDTOResponse>> getDisciplinasByAluno(@PathVariable Long id) throws ResourceNotFound {
        List<DisciplinaDTOResponse> disciplinas = service.disciplinasByUser(id);
        return ResponseEntity.ok(disciplinas);
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


    @PutMapping("/alunos/{id}")
    public ResponseEntity<ReturnAlunoDTO> atualizarAluno(@PathVariable Long id, @Valid @RequestBody InputAlunoDTO dto) throws ResourceNotFound {
        ReturnAlunoDTO alunoAtualizado = service.atualizarAluno(id, dto);
        return ResponseEntity.ok(alunoAtualizado);
    }

    @PutMapping("/professores/{id}")
    public ResponseEntity<ReturnProfessorDTO> atualizarProfessor(@PathVariable Long id, @Valid @RequestBody InputProfessorDTO dto) throws ResourceNotFound {
        ReturnProfessorDTO professorAtualizado = service.atualizarProfessor(id, dto);
        return ResponseEntity.ok(professorAtualizado);
    }
}