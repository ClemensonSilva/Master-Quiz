package br.com.edu.ufersa.projeto_quiz.API.controller;

import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTO;
import br.com.edu.ufersa.projeto_quiz.API.dto.QuestaoDTOResponse;
import br.com.edu.ufersa.projeto_quiz.Service.QuestaoService;
import br.com.edu.ufersa.projeto_quiz.exception.ResourceNotFound;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
/**
 * Controlador responsável por gerenciar as questões associadas a uma disciplina.
 * Fornece endpoints para listagem, consulta, criação, edição e remoção de questões.
 */
@RestController
@RequestMapping("/api/v1/disciplinas/{disciplinaId}/questoes")
public class QuestaoController {

    @Autowired
    private QuestaoService service;

    /**
     * Lista todas as questões pertencentes a uma disciplina.
     *
     * @param disciplinaId ID da disciplina cujas questões serão retornadas.
     * @return ResponseEntity contendo uma lista de {@link QuestaoDTOResponse}.
     * @throws ResourceNotFound caso a disciplina informada não seja encontrada.
     */
    @GetMapping
    public ResponseEntity<List<QuestaoDTOResponse>> findAll(@PathVariable long disciplinaId) throws ResourceNotFound {
        List<QuestaoDTOResponse> questoes = service.findAll(disciplinaId);
        return ResponseEntity.ok(questoes);
    }

    /**
     * Recupera uma questão específica pertencente a uma disciplina.
     *
     * @param disciplinaId ID da disciplina que contém a questão.
     * @param id ID da questão a ser buscada.
     * @return ResponseEntity contendo a {@link QuestaoDTOResponse} encontrada.
     * @throws ResourceNotFound caso a questão não seja encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestaoDTOResponse> findById(@PathVariable long disciplinaId,
                                                       @PathVariable Long id) throws ResourceNotFound {
        QuestaoDTOResponse questao = service.findById(id);
        return ResponseEntity.ok(questao);
    }

    /**
     * Remove uma questão existente com base em seu ID.
     *
     * @param disciplinaId ID da disciplina que contém a questão.
     * @param id ID da questão a ser excluída.
     * @return ResponseEntity vazio com status 204 caso a remoção seja bem-sucedida.
     * @throws ResourceNotFound caso a questão não seja encontrada.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long disciplinaId, @PathVariable Long id) throws ResourceNotFound {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Atualiza os dados de uma questão existente.
     *
     * @param disciplinaId ID da disciplina que contém a questão.
     * @param id ID da questão a ser atualizada.
     * @param dto objeto contendo os novos dados da questão.
     * @return ResponseEntity contendo a {@link QuestaoDTOResponse} atualizada.
     * @throws ResourceNotFound caso a questão não seja encontrada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuestaoDTOResponse> edit(@PathVariable long disciplinaId,
                                                   @PathVariable Long id,
                                                   @Valid @RequestBody QuestaoDTO dto) throws ResourceNotFound {

        QuestaoDTOResponse questaoDTOResponse = service.edit(id, dto);
        addSelfLink(questaoDTOResponse, id, disciplinaId);
        return ResponseEntity.ok(questaoDTOResponse);
    }

    /**
     * Cria uma nova questão para determinada disciplina.
     *
     * @param disciplinaId ID da disciplina onde a questão será criada.
     * @param dto dados da questão a ser criada.
     * @return ResponseEntity contendo a {@link QuestaoDTOResponse} criada.
     * @throws ResourceNotFound caso a disciplina não seja encontrada.
     */
    @PostMapping
    public ResponseEntity<QuestaoDTOResponse> create(@PathVariable long disciplinaId,
                                                     @Valid @RequestBody QuestaoDTO dto) throws ResourceNotFound {

        QuestaoDTOResponse questaoDTOResponse = service.save(dto, disciplinaId);
        addSelfLink(questaoDTOResponse, questaoDTOResponse.getId(), disciplinaId);
        return ResponseEntity.ok(questaoDTOResponse);
    }

    /**
     * Adiciona o link HATEOAS de auto-referência à resposta da questão.
     *
     * @param questao objeto de resposta da questão.
     * @param id ID da questão.
     * @param disciplinaId ID da disciplina que contém a questão.
     * @throws ResourceNotFound caso ocorra falha ao gerar o link.
     */
    public void addSelfLink(QuestaoDTOResponse questao, long id, long disciplinaId) throws ResourceNotFound {
        questao.add(linkTo(methodOn(QuestaoController.class).findById(disciplinaId, id)).withSelfRel());
    }
}
