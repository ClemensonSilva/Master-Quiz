package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.*;
import br.com.edu.ufersa.projeto_quiz.Service.UsuarioService;
import br.com.edu.ufersa.projeto_quiz.exception.BusinessLogicException;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controlador responsável pelas operações relacionadas aos usuários do sistema,
 * incluindo alunos e professores.
 *
 * <p>Permite buscar, criar, editar e excluir usuários, além de manipular vínculos com disciplinas
 * e operações específicas de cada tipo de usuário.</p>
 */
@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    /**
     * Construtor para injeção do serviço de usuários.
     *
     * @param service instância de {@link UsuarioService}
     */
    @Autowired
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    /**
     * Retorna todos os alunos cadastrados no sistema.
     *
     * @return lista de alunos com links HATEOAS para cada recurso.
     * @throws ResourceNotFound se não houver alunos cadastrados.
     */
    @GetMapping("/alunos")
    public ResponseEntity<List<ReturnAlunoDTO>> getAlunos() throws ResourceNotFound {
        List<ReturnAlunoDTO> alunos = service.listarTodosAlunos();

        for (ReturnAlunoDTO aluno : alunos) {
            Link selfAlunoLink = linkTo(methodOn(UsuarioController.class).getAluno(aluno.getId())).withSelfRel();
            aluno.add(selfAlunoLink);
        }

        return ResponseEntity.ok(alunos);
    }

    /**
     * Retorna todos os professores cadastrados no sistema.
     *
     * @return lista de professores com links HATEOAS.
     * @throws ResourceNotFound se não houver professores cadastrados.
     */
    @GetMapping("/professores")
    public ResponseEntity<List<ReturnProfessorDTO>> getProfessores() throws ResourceNotFound {
        List<ReturnProfessorDTO> professores = service.listarTodosProfessores();

        for (ReturnProfessorDTO professor : professores) {
            Link selfProfessorLink = linkTo(methodOn(UsuarioController.class).getProfessor(professor.getId())).withSelfRel();
            professor.add(selfProfessorLink);
        }

        return ResponseEntity.ok(professores);
    }

    /**
     * Retorna um aluno específico pelo seu ID.
     *
     * @param id identificador do aluno.
     * @return aluno encontrado com link para listar todos os alunos.
     * @throws ResourceNotFound se o aluno não existir.
     */
    @GetMapping("/alunos/{id}")
    public ResponseEntity<ReturnAlunoDTO> getAluno(@PathVariable Long id) throws ResourceNotFound {
        ReturnAlunoDTO aluno = service.getAluno(id);
        aluno.add(linkTo(methodOn(UsuarioController.class).getAlunos()).withRel("allAlunos"));
        return ResponseEntity.ok(aluno);
    }
    /**
     * Retorna um aluno específico pelo seu email.
     *
     * @param email email do aluno.
     * @return aluno encontrado com link para listar todos os alunos.
     * @throws ResourceNotFound se o professor não existir.
     */
    @GetMapping("/alunos/email/{email}")
    public ResponseEntity<ReturnAlunoDTO> getAlunoByEmail(@PathVariable String email) throws ResourceNotFound {
        ReturnAlunoDTO aluno = service.getAlunoByEmail(email);
        aluno.add(linkTo(methodOn(UsuarioController.class).getAlunos()).withRel("allAlunos"));
        return ResponseEntity.ok(aluno);
    }

    /**
     * Retorna um professor específico pelo seu ID.
     *
     * @param id identificador do professor.
     * @return professor encontrado com link para listar todos os professores.
     * @throws ResourceNotFound se o professor não existir.
     */
    @GetMapping("/professores/{id}")
    public ResponseEntity<ReturnProfessorDTO> getProfessor(@PathVariable Long id) throws ResourceNotFound {
        ReturnProfessorDTO professor = service.getProfessor(id);
        professor.add(linkTo(methodOn(UsuarioController.class).getProfessores()).withRel("allprofessores"));
        return ResponseEntity.ok(professor);
    }
    /**
     * Retorna um professor específico pelo seu email.
     *
     * @param email email do professor.
     * @return professor encontrado com link para listar todos os professores.
     * @throws ResourceNotFound se o professor não existir.
     */
    @GetMapping("/professores/email/{email}")
    public ResponseEntity<ReturnProfessorDTO> getProfessorByEmail(@PathVariable String email) throws ResourceNotFound {
        ReturnProfessorDTO professor = service.getProfessorByEmail(email);
        professor.add(linkTo(methodOn(UsuarioController.class).getProfessores()).withRel("allprofessores"));
        return ResponseEntity.ok(professor);
    }

    /**
     * Lista todas as disciplinas associadas a um professor.
     *
     * @param id identificador do professor.
     * @return lista de disciplinas com links HATEOAS úteis.
     * @throws ResourceNotFound se o professor ou disciplinas não forem encontrados.
     */
    @GetMapping("/professores/{id}/disciplinas")
    public ResponseEntity<List<DisciplinaDTOResponse>> getDisciplinasByProfessor(@PathVariable Long id,
                                                                                 @RequestParam(required = false) String nome
    ) throws ResourceNotFound {
        List<DisciplinaDTOResponse> disciplinas = service.disciplinasByUser(id, nome);

        for (DisciplinaDTOResponse disciplina : disciplinas) {
            disciplina.add(linkTo(methodOn(DisciplinaController.class).findById(disciplina.getId())).withSelfRel());
            disciplina.add(linkTo(methodOn(DisciplinaController.class).getProfessoresByDisciplina(id)).withRel("professor"));
            disciplina.add(linkTo(methodOn(QuizController.class).findAll(disciplina.getId())).withRel("quizzes"));
            disciplina.add(linkTo(methodOn(DisciplinaController.class).getAlunosByDisciplina(disciplina.getId())).withRel("alunos"));
        }

        return ResponseEntity.ok(disciplinas);
    }

    /**
     * Lista todas as disciplinas associadas a um aluno.
     * Permite que o aluno também faça pesquisas usando o nome da disciplina
     *
     * @param id identificador do aluno.
     * @return lista de disciplinas com links básicos.
     * @throws ResourceNotFound se o aluno não existir.
     */
    @GetMapping("/alunos/{id}/disciplinas")
    public ResponseEntity<List<DisciplinaDTOResponse>> getDisciplinasByAluno(@PathVariable Long id,
                                                                             @RequestParam(required = false) String nome
    ) throws ResourceNotFound {
        List<DisciplinaDTOResponse> disciplinas = service.disciplinasByUser(id, nome);

        for (DisciplinaDTOResponse disciplina : disciplinas) {
            disciplina.add(linkTo(methodOn(DisciplinaController.class).findById(disciplina.getId())).withSelfRel());
        }

        return ResponseEntity.ok(disciplinas);
    }


    /**
     * Realiza a matrícula de um aluno em uma disciplina.
     *
     * @param matriculaInputDTO dados da matrícula.
     * @param id identificador do aluno.
     * @return matrícula criada com status 201 CREATED.
     * @throws ResourceNotFound se aluno ou disciplina não existirem.
     * @throws BusinessLogicException se o aluno já estiver matriculado.
     */
    @PostMapping("/alunos/{id}/disciplinas")
    public ResponseEntity<MatriculaResponseDTO> enroll(@Valid @RequestBody MatriculaInputDTO matriculaInputDTO,
                                                       @PathVariable long id)
            throws ResourceNotFound, BusinessLogicException {

        return new ResponseEntity<>(service.enrollAluno(matriculaInputDTO, id), HttpStatus.CREATED);
    }

    /**
     * Cria uma nova disciplina associada a um professor.
     *
     * @param disciplinaDTO dados da disciplina.
     * @param id identificador do professor.
     * @return disciplina criada com status 201 CREATED.
     * @throws BusinessLogicException se regras de criação forem violadas.
     */
    @PostMapping("/professores/{id}")
    public ResponseEntity<DisciplinaDTOResponse> createDisciplina(@RequestBody DisciplinaDTO disciplinaDTO,
                                                                  @PathVariable long id) throws BusinessLogicException {
        return new ResponseEntity<>(service.createDisciplina(disciplinaDTO, id), HttpStatus.CREATED);
    }

    /**
     * Cria um novo aluno.
     *
     * @param alunoDTO dados do aluno.
     * @return aluno criado com link para consulta individual.
     * @throws ResourceNotFound exceção genérica de validação.
     */
    @PostMapping("/alunos")
    public ResponseEntity<ReturnAlunoDTO> criarAluno(@Valid @RequestBody InputAlunoDTO alunoDTO)
            throws ResourceNotFound {

        ReturnAlunoDTO alunoSalvo = service.criarAluno(alunoDTO);
        alunoSalvo.add(linkTo(methodOn(UsuarioController.class).getAluno(alunoSalvo.getId())).withSelfRel());

        return new ResponseEntity<>(alunoSalvo, HttpStatus.CREATED);
    }

    /**
     * Cria um novo professor.
     *
     * @param professorDTO dados do professor.
     * @return professor criado com link para consulta individual.
     * @throws ResourceNotFound exceção genérica de validação.
     */
    @PostMapping("/professores")
    public ResponseEntity<ReturnProfessorDTO> criarProfessor(@Valid @RequestBody InputProfessorDTO professorDTO)
            throws ResourceNotFound {

        ReturnProfessorDTO professorSalvo = service.criarProfessor(professorDTO);
        professorSalvo.add(linkTo(methodOn(UsuarioController.class).getProfessor(professorSalvo.getId())).withSelfRel());

        return new ResponseEntity<>(professorSalvo, HttpStatus.CREATED);
    }

    /**
     * Exclui um usuário (aluno ou professor) pelo ID.
     *
     * @param id identificador do usuário.
     * @return resposta 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Atualiza os dados de um aluno existente.
     *
     * @param id identificador do aluno.
     * @param dto dados atualizados.
     * @return aluno atualizado com link HATEOAS.
     * @throws ResourceNotFound se o aluno não existir.
     */
    @PutMapping("/alunos/{id}")
    public ResponseEntity<ReturnAlunoDTO> atualizarAluno(@PathVariable Long id,
                                                         @Valid @RequestBody InputAlunoDTO dto)
            throws ResourceNotFound {

        ReturnAlunoDTO alunoAtualizado = service.atualizarAluno(id, dto);
        alunoAtualizado.add(linkTo(methodOn(UsuarioController.class).getAluno(alunoAtualizado.getId())).withSelfRel());

        return ResponseEntity.ok(alunoAtualizado);
    }

    /**
     * Atualiza os dados de um professor existente.
     *
     * @param id identificador do professor.
     * @param dto dados atualizados.
     * @return professor atualizado com link HATEOAS.
     * @throws ResourceNotFound se o professor não existir.
     */
    @PutMapping("/professores/{id}")
    public ResponseEntity<ReturnProfessorDTO> atualizarProfessor(@PathVariable Long id,
                                                                 @Valid @RequestBody InputProfessorDTO dto)
            throws ResourceNotFound {

        ReturnProfessorDTO professorAtualizado = service.atualizarProfessor(id, dto);
        professorAtualizado.add(linkTo(methodOn(UsuarioController.class).getProfessor(professorAtualizado.getId())).withSelfRel());

        return ResponseEntity.ok(professorAtualizado);
    }
}
