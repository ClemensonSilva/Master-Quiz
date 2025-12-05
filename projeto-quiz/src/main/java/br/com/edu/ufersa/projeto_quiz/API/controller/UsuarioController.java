package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.*;
import br.com.edu.ufersa.projeto_quiz.Service.UsuarioService;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    @Autowired
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }


    @GetMapping("/alunos")
    public ResponseEntity<List<ReturnAlunoDTO>> getAlunos() throws ResourceNotFound {
        List<ReturnAlunoDTO> alunos = service.listarTodosAlunos();
        for (ReturnAlunoDTO aluno: alunos){
            Link selfAlunoLink = linkTo(methodOn(UsuarioController.class).getAluno(aluno.getId())).withSelfRel();
            aluno.add(selfAlunoLink);
        }
        return ResponseEntity.ok(alunos);
    }
    @GetMapping("/professores")
    public ResponseEntity<List<ReturnProfessorDTO>> getProfessores() throws ResourceNotFound {
        List<ReturnProfessorDTO> professores = service.listarTodosProfessores();
        for (ReturnProfessorDTO professor: professores){
            Link selfProfessorLink = linkTo(methodOn(UsuarioController.class).getProfessor(professor.getId())).withSelfRel();
            professor.add(selfProfessorLink);
        }
        return ResponseEntity.ok(professores);
    }

    @GetMapping("/alunos/{id}")
    public ResponseEntity<ReturnAlunoDTO> getAluno(@PathVariable Long id) throws ResourceNotFound {
        ReturnAlunoDTO aluno = service.getAluno(id);
        aluno.add(linkTo(methodOn(UsuarioController.class).getAlunos()).withRel("allAlunos"));
        return ResponseEntity.ok(aluno);
    }
    @GetMapping("/professores/{id}")
    public ResponseEntity<ReturnProfessorDTO> getProfessor(@PathVariable Long id) throws ResourceNotFound {
        ReturnProfessorDTO professor = service.getProfessor(id);

        professor.add(linkTo(methodOn(UsuarioController.class).getProfessores()).withRel("allprofessores"));
        return ResponseEntity.ok(professor);
    }
    // TODO corrigir DisciplinaDTOResponse para retornar apenas id e nome, demais atributos serão links
    // além disso, é preciso criar alguns metodos para encontrar o professor atravez do Id da disciplina ( isso deve ser feito no controller de disciplina)
    @GetMapping("/professores/{id}/disciplinas")
    public ResponseEntity<List<DisciplinaDTOResponse>> getDisciplinasByProfessor(@PathVariable Long id) throws ResourceNotFound {
        List<DisciplinaDTOResponse> disciplinas = service.disciplinasByUser(id);

        for (DisciplinaDTOResponse disciplina: disciplinas){
            disciplina.add(linkTo(methodOn(DisciplinaController.class).findById(disciplina.getId())).withSelfRel());
            disciplina.add(linkTo(methodOn(DisciplinaController.class).getProfessoresByDisciplina(disciplina.getId())).withRel("professor"));
            disciplina.add(linkTo(methodOn(DisciplinaController.class).getQuizesByDisciplina(disciplina.getId())).withRel("quizzes"));
            disciplina.add(linkTo(methodOn(DisciplinaController.class).getAlunosByDisciplina(disciplina.getId())).withRel("alunos"));
        }
        return ResponseEntity.ok(disciplinas);
    }
    @GetMapping("/alunos/{id}/disciplinas")
    public ResponseEntity<List<DisciplinaDTOResponse>> getDisciplinasByAluno(@PathVariable Long id) throws ResourceNotFound {
        List<DisciplinaDTOResponse> disciplinas = service.disciplinasByUser(id);

        for (DisciplinaDTOResponse disciplina: disciplinas){
            disciplina.add(linkTo(methodOn(DisciplinaController.class).findById(disciplina.getId())).withSelfRel());
        }
        return ResponseEntity.ok(disciplinas);
    }

    @PostMapping("/alunos/{id}/disciplinas")
    public ResponseEntity<MatriculaResponseDTO> enroll(@Valid @RequestBody MatriculaInputDTO matriculaInputDTO, @PathVariable long id) throws ResourceNotFound {
        return new ResponseEntity<>(service.enrollAluno(matriculaInputDTO, id), HttpStatus.CREATED);
    }

    @PostMapping("/alunos")
    public ResponseEntity<ReturnAlunoDTO> criarAluno(@Valid @RequestBody InputAlunoDTO alunoDTO) throws ResourceNotFound {
        ReturnAlunoDTO alunoSalvo = service.criarAluno(alunoDTO);
        Link selfAlunoLink = linkTo(methodOn(UsuarioController.class).getAluno(alunoSalvo.getId())).withSelfRel();
        alunoSalvo.add(selfAlunoLink);

        return new ResponseEntity<>(alunoSalvo, HttpStatus.CREATED);
    }

    @PostMapping("/professores")
    public ResponseEntity<ReturnProfessorDTO> criarProfessor(@Valid @RequestBody InputProfessorDTO professorDTO) throws ResourceNotFound {
        ReturnProfessorDTO professorSalvo = service.criarProfessor(professorDTO);
        Link selfProfessorLink = linkTo(methodOn(UsuarioController.class).getProfessor(professorSalvo.getId())).withSelfRel();
        professorSalvo.add(selfProfessorLink);
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
        Link selfAlunoLink = linkTo(methodOn(UsuarioController.class).getAluno(alunoAtualizado.getId())).withSelfRel();
        alunoAtualizado.add(selfAlunoLink);

        return ResponseEntity.ok(alunoAtualizado);
    }

    @PutMapping("/professores/{id}")
    public ResponseEntity<ReturnProfessorDTO> atualizarProfessor(@PathVariable Long id, @Valid @RequestBody InputProfessorDTO dto) throws ResourceNotFound {
        ReturnProfessorDTO professorAtualizado = service.atualizarProfessor(id, dto);
        Link selfProfessorLink = linkTo(methodOn(UsuarioController.class).getProfessor(professorAtualizado.getId())).withSelfRel();
        professorAtualizado.add(selfProfessorLink);

        return ResponseEntity.ok(professorAtualizado);
    }
}