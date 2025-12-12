package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.*;
import br.com.edu.ufersa.projeto_quiz.Service.DisciplinaService;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por gerenciar operações relacionadas às disciplinas.
 *
 * <p>Este controlador expõe endpoints para listar, consultar, criar, editar e remover disciplinas,
 * além de retornar alunos, professores e quizzes vinculados a uma disciplina específica.</p>
 *
 * @author
 */
@RestController
@RequestMapping("/api/v1/disciplinas")
public class DisciplinaController {

    @Autowired
    private DisciplinaService service;

    /**
     * Retorna todas as disciplinas cadastradas.
     *
     * @return lista de {@link DisciplinaDTOResponse} com status 200 OK.
     */
    @GetMapping()
    public ResponseEntity<List<DisciplinaDTOResponse>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    /**
     * Busca uma disciplina pelo ID informado.
     *
     * @param id identificador da disciplina.
     * @return representação da disciplina encontrada.
     * @throws ResourceNotFound caso a disciplina não exista.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DisciplinaDTOResponse> findById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    /**
     * Retorna todos os alunos matriculados em uma disciplina específica.
     *
     * @param id identificador da disciplina.
     * @return lista de alunos.
     * @throws ResourceNotFound caso a disciplina não exista.
     */
    @GetMapping("/{id}/alunos")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<List<ReturnAlunoDTO>> getAlunosByDisciplina(@PathVariable long id) throws ResourceNotFound {
        return new ResponseEntity<>(service.getAlunosByDisciplina(id), HttpStatus.OK);
    }

    /**
     * Retorna o professor responsável por uma disciplina.
     *
     * @param id identificador da disciplina.
     * @return DTO com informações do professor.
     * @throws ResourceNotFound caso a disciplina não exista.
     */
    @GetMapping("/{id}/professores")
    public ResponseEntity<ReturnProfessorDTO> getProfessoresByDisciplina(@PathVariable long id) throws ResourceNotFound {
        return new ResponseEntity<>(service.getProfessorByDisciplina(id), HttpStatus.OK);
    }

    /**
     * Adiciona um quiz a uma disciplina específica.
     *
     * <p>No futuro, este endpoint deverá validar o professor responsável
     * através do {@code UsuarioController}.</p>
     *
     * @param quizDTO dados do quiz que será criado.
     * @param id identificador da disciplina.
     * @return quiz criado com status 201 CREATED.
     * @throws ResourceNotFound caso a disciplina não exista.
     */
    @PostMapping("/{id}/quizes")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<QuizDTO> addQuiz(@RequestBody QuizDTO quizDTO, @PathVariable long id) throws ResourceNotFound {
        return new ResponseEntity<>(service.addQuiz(quizDTO, id), HttpStatus.CREATED);
    }

    /**
     * Atualiza os dados de uma disciplina existente.
     *
     * @param disciplinaDTO dados atualizados.
     * @param id identificador da disciplina a ser editada.
     * @return disciplina editada.
     * @throws ResourceNotFound caso a disciplina não exista.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<DisciplinaDTOResponse> edit(@RequestBody DisciplinaDTO disciplinaDTO, @PathVariable Long id) throws ResourceNotFound {
        return new ResponseEntity<>(service.edit(disciplinaDTO, id), HttpStatus.OK);
    }

    /**
     * Remove uma disciplina pelo ID informado.
     *
     * @param id identificador da disciplina.
     * @return resposta sem conteúdo com status 204 NO CONTENT.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('PROFESSOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
