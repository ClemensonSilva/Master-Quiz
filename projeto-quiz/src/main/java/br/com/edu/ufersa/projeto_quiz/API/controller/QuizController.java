package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuizDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Service.QuizService;
import br.com.edu.ufersa.projeto_quiz.exception.BusinessLogicException;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controlador responsável por operações relacionadas aos quizzes
 * associados a uma disciplina específica.
 *
 * <p>Este controller permite listar, consultar, editar e excluir quizzes,
 * bem como adicionar e remover questões de um quiz. Cada quiz está sempre
 * vinculado a uma disciplina, representada pelo parâmetro {@code disciplinaId}.</p>
 */
@RestController
@RequestMapping("/api/v1/disciplinas/{disciplinaId}/quizes")
public class QuizController {

    private final QuizService service;

    /**
     * Construtor para injeção do serviço de quizzes.
     *
     * @param service instância de {@link QuizService}.
     */
    @Autowired
    public QuizController(QuizService service) {
        this.service = service;
    }

    /**
     * Lista todos os quizzes associados à disciplina informada.
     *
     * @param disciplinaId identificador da disciplina.
     * @return lista de quizzes, ou 204 No Content caso vazia.
     * @throws ResourceNotFound se a disciplina não existir.
     */
    @GetMapping
    public ResponseEntity<List<QuizDTOResponse>> findAll(@PathVariable Long disciplinaId) throws ResourceNotFound {
        List<QuizDTOResponse> quizList = service.findAll(disciplinaId);

        if (quizList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        for (QuizDTOResponse quiz : quizList) {
            long quizId = quiz.getId();
            quiz.add(linkTo(methodOn(QuizController.class).findById(quizId, disciplinaId)).withSelfRel());
            adicionarLinkDisciplina(quiz, disciplinaId);
        }

        return ResponseEntity.ok(quizList);
    }

    /**
     * Retorna um quiz específico pelo seu ID.
     *
     * @param id identificador do quiz.
     * @param disciplinaId identificador da disciplina associada.
     * @return quiz encontrado com links HATEOAS.
     * @throws ResourceNotFound se o quiz ou a disciplina não existirem.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuizDTOResponse> findById(@PathVariable Long id,
                                                    @PathVariable Long disciplinaId) throws ResourceNotFound {
        QuizDTOResponse quiz = service.findById(id);

        quiz.add(linkTo(methodOn(QuizController.class).findById(id, disciplinaId)).withSelfRel());
        quiz.add(linkTo(methodOn(QuizController.class).findAll(disciplinaId)).withRel("lista-quizes"));
        adicionarLinkDisciplina(quiz, disciplinaId);
        quiz.add(linkTo(methodOn(QuizController.class).delete(id, disciplinaId)).withRel("deletar"));

        return ResponseEntity.ok(quiz);
    }

    /**
     * Adiciona uma questão a um quiz.
     *
     * @param disciplinaId identificador da disciplina.
     * @param id identificador do quiz.
     * @param questaoId identificador da questão.
     * @return quiz atualizado com status 201 CREATED.
     * @throws ResourceNotFound se quiz, questão ou disciplina não existirem.
     * @throws BusinessLogicException se regras de negócio forem violadas (ex: questão duplicada).
     */
    @PostMapping("/{id}/questoes/{questaoId}")
    public ResponseEntity<QuizDTOResponse> addQuestao(@PathVariable long disciplinaId,
                                                      @PathVariable long id,
                                                      @PathVariable long questaoId)
            throws ResourceNotFound, BusinessLogicException {

        return new ResponseEntity<>(service.addQuestao(disciplinaId, id, questaoId), HttpStatus.CREATED);
    }

    /**
     * Remove uma questão de um quiz.
     *
     * @param disciplinaId identificador da disciplina.
     * @param id identificador do quiz.
     * @param questaoId identificador da questão.
     * @return 204 No Content em caso de sucesso.
     * @throws ResourceNotFound se quiz, disciplina ou questão não forem encontrados.
     * @throws BusinessLogicException se houver inconsistências na remoção.
     */
    @DeleteMapping("/{id}/questoes/{questaoId}")
    public ResponseEntity<Void> removeQuestao(@PathVariable long disciplinaId,
                                              @PathVariable long id,
                                              @PathVariable long questaoId)
            throws ResourceNotFound, BusinessLogicException {

        service.removeQuestao(disciplinaId, id, questaoId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Atualiza os dados de um quiz.
     *
     * @param quizDTO objeto contendo as informações atualizadas.
     * @param disciplinaId identificador da disciplina associada.
     * @param id identificador do quiz.
     * @return quiz atualizado com status 200 OK.
     * @throws ResourceNotFound caso o quiz ou disciplina não existam.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuizDTOResponse> edit(@RequestBody QuizDTO quizDTO,
                                                @PathVariable long disciplinaId,
                                                @PathVariable long id) throws ResourceNotFound {

        return new ResponseEntity<>(service.update(quizDTO, id, disciplinaId), HttpStatus.OK);
    }

    /**
     * Exclui um quiz pelo ID.
     *
     * @param id identificador do quiz.
     * @param disciplinaId identificador da disciplina.
     * @return resposta sem conteúdo com status 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @PathVariable Long disciplinaId) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adiciona ao DTO do quiz um link HATEOAS apontando para a disciplina.
     *
     * @param quiz objeto que receberá o link.
     * @param disciplinaId identificador da disciplina.
     */
    private void adicionarLinkDisciplina(QuizDTOResponse quiz, Long disciplinaId) {
        Link disciplinaLink = linkTo(methodOn(DisciplinaController.class)
                .findById(disciplinaId))
                .withRel("disciplina");

        quiz.add(disciplinaLink);
    }
}
